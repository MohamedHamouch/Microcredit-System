package services;

import entities.enums.StatutPaiement;
import entities.enums.TypeIncident;
import entities.models.Credit;
import entities.models.Echeance;
import entities.models.Incident;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import repository.EcheanceRepository;
import repository.IncidentRepository;

public class EcheanceService {
    
    private final EcheanceRepository echeanceRepository;
    private final IncidentRepository incidentRepository;
    
    public EcheanceService() {
        this.echeanceRepository = new EcheanceRepository();
        this.incidentRepository = new IncidentRepository();
    }
    
    public void generatePaymentSchedule(Credit credit) throws SQLException {
        if (credit == null || credit.getId() == null) {
            throw new IllegalArgumentException("Credit invalide pour generation echeancier");
        }
        
        double totalWithInterest = credit.getMontantOctroye() + (credit.getMontantOctroye() * credit.getTauxInteret());
        double mensualite = totalWithInterest / credit.getDureeEnMois();
        
        LocalDate firstPaymentDate = credit.getDateCredit().plusMonths(1);
        
        for (int i = 0; i < credit.getDureeEnMois(); i++) {
            Echeance echeance = new Echeance();
            echeance.setCreditId(credit.getId());
            echeance.setDateEcheance(firstPaymentDate.plusMonths(i));
            echeance.setMensualite(mensualite);
            
            try {
                echeanceRepository.create(echeance, credit.getId());
            } catch (Exception e) {
                System.err.println("Erreur creation echeance " + (i+1) + ": " + e.getMessage());
                throw new SQLException("Impossible de creer l'echeancier", e);
            }
        }
        
        System.out.println("Echeancier cree: " + credit.getDureeEnMois() + " echeances");
    }
    
    public List<Echeance> getUnpaidEcheancesByCreditId(int creditId) throws SQLException {
        List<Echeance> echeances = echeanceRepository.findByCreditId(creditId);
        List<Echeance> unpaidEcheances = new ArrayList<>();
        
        for (Echeance echeance : echeances) {
            if (echeance.getDateDePaiment() == null) {
                unpaidEcheances.add(echeance);
            }
        }
        
        return unpaidEcheances;
    }
    
    public List<Echeance> getEcheancesByCreditId(int creditId) throws SQLException {
        return echeanceRepository.findByCreditId(creditId);
    }
    
    public Echeance findEcheanceById(int id) throws SQLException {
        return echeanceRepository.findById(id);
    }
    
    public void recordPayment(int echeanceId, LocalDate paymentDate) throws SQLException {
        Echeance echeance = echeanceRepository.findById(echeanceId);
        if (echeance == null) {
            throw new IllegalArgumentException("Echeance non trouvee: " + echeanceId);
        }
        
        LocalDate dueDate = echeance.getDateEcheance();
        LocalDate paidDate = paymentDate;
        
        int daysLate = 0;
        if (paidDate.isAfter(dueDate)) {
            LocalDate temp = dueDate;
            while (temp.isBefore(paidDate)) {
                daysLate++;
                temp = temp.plusDays(1);
            }
        }
        
        StatutPaiement newStatus;
        TypeIncident incidentType;
        
        if (daysLate <= 5) {
            newStatus = StatutPaiement.PAYE_A_TEMPS;
            incidentType = TypeIncident.PAYE_A_TEMPS;
        } else if (daysLate <= 30) {
            newStatus = StatutPaiement.PAYE_EN_RETARD;
            incidentType = TypeIncident.PAYE_EN_RETARD;
        } else {
            newStatus = StatutPaiement.IMPAYE_REGLE;
            incidentType = TypeIncident.IMPAYE_REGLE;
        }
        
        echeance.setDateDePaiment(paymentDate);
        echeance.setStatutPaiement(newStatus);
        echeanceRepository.update(echeance);
        
        Incident incident = new Incident();
        incident.setDateIncident(paymentDate);
        incident.setEcheanceId(echeanceId);
        incident.setTypeIncident(incidentType);
        incident.setScore(calculateIncidentScore(incidentType));
        incidentRepository.create(incident);
        
        System.out.println("Paiement enregistre avec succes!");
        System.out.println("Statut: " + newStatus.getMessage());
        if (daysLate > 0) {
            System.out.println("Retard: " + daysLate + " jours");
        }
    }
    
    private int calculateIncidentScore(TypeIncident type) {
        switch (type) {
            case PAYE_A_TEMPS:
                return 0;
            case PAYE_EN_RETARD:
                return -3;
            case IMPAYE_REGLE:
                return -5;
            case IMPAYE_NON_REGLE:
                return -10;
            default:
                return 0;
        }
    }
    
    public void updateEcheanceStatuses() throws SQLException {
        List<Echeance> allEcheances = echeanceRepository.findAll();
        LocalDate today = LocalDate.now();
        
        int updated = 0;
        int incidentsCreated = 0;
        
        for (Echeance echeance : allEcheances) {
            if (echeance.getDateDePaiment() != null) {
                continue;
            }
            
            LocalDate dueDate = echeance.getDateEcheance();
            
            int daysLate = 0;
            if (today.isAfter(dueDate)) {
                LocalDate temp = dueDate;
                while (temp.isBefore(today)) {
                    daysLate++;
                    temp = temp.plusDays(1);
                }
            }
            
            StatutPaiement oldStatus = echeance.getStatutPaiement();
            StatutPaiement newStatus = null;
            boolean createIncident = false;
            
            if (daysLate > 30) {
                newStatus = StatutPaiement.IMPAYE_NON_REGLE;
                if (oldStatus != StatutPaiement.IMPAYE_NON_REGLE) {
                    createIncident = true;
                }
            } else if (daysLate > 5) {
                newStatus = StatutPaiement.EN_RETARD;
                if (oldStatus != StatutPaiement.EN_RETARD && oldStatus != StatutPaiement.IMPAYE_NON_REGLE) {
                    createIncident = true;
                }
            }
            
            if (newStatus != null && newStatus != oldStatus) {
                echeance.setStatutPaiement(newStatus);
                echeanceRepository.update(echeance);
                updated++;
                
                if (createIncident) {
                    Incident incident = new Incident();
                    incident.setDateIncident(today);
                    incident.setEcheanceId(echeance.getId());
                    
                    if (newStatus == StatutPaiement.IMPAYE_NON_REGLE) {
                        incident.setTypeIncident(TypeIncident.IMPAYE_NON_REGLE);
                    } else if (newStatus == StatutPaiement.EN_RETARD) {
                        incident.setTypeIncident(TypeIncident.EN_RETARD);
                    }
                    
                    incident.setScore(calculateIncidentScore(incident.getTypeIncident()));
                    incidentRepository.create(incident);
                    incidentsCreated++;
                }
            }
        }
        
        System.out.println("Verification terminee:");
        System.out.println("- Echeances mises a jour: " + updated);
        System.out.println("- Incidents crees: " + incidentsCreated);
    }
}
