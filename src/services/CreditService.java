package services;

import entities.enums.Decision;
import entities.enums.TypeCredit;
import entities.models.Credit;
import entities.models.Personne;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import repository.CreditRepository;
import repository.PersonneRepository;

public class CreditService {

    private CreditRepository creditRepository;
    private PersonneRepository personneRepository;
    private EcheanceService echeanceService;

    public CreditService() {
        this.creditRepository = new CreditRepository();
        this.personneRepository = new PersonneRepository();
        this.echeanceService = new EcheanceService();
    }

    public Credit processCreditRequest(int personneId, double montantDemande,
            double tauxInteret, TypeCredit typeCredit, int dureeEnMois) throws SQLException {

        Optional<Personne> personneOpt = personneRepository.findById(personneId);
        if (!personneOpt.isPresent()) {
            throw new IllegalArgumentException("Client non trouvé avec ID: " + personneId);
        }

        Personne personne = personneOpt.get();
        int score = personne.getScore();

        Decision decision;
        double montantOctroye;

        if (score >= 80) {
            decision = Decision.ACCORD_IMMEDIAT;
            montantOctroye = montantDemande;
        } else if (score >= 60) {
            decision = Decision.ETUDE_MANUELLE;
            montantOctroye = montantDemande;
        } else {
            decision = Decision.REFUS_AUTOMATIQUE;
            montantOctroye = 0.0;
        }
        Credit credit = new Credit();
        credit.setPersonneId(personneId);
        credit.setDateCredit(LocalDate.now());
        credit.setMontantDemande(montantDemande);
        credit.setMontantOctroye(montantOctroye);
        credit.setTauxInteret(tauxInteret);
        credit.setDureeEnMois(dureeEnMois);
        credit.setTypeCredit(typeCredit.getLabel());
        credit.setDecision(decision);

        Credit savedCredit;
        try {
            savedCredit = creditRepository.create(credit);
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de la création du crédit: " + e.getMessage());
        }
        if (decision == Decision.ACCORD_IMMEDIAT) {
            if (montantOctroye > 0 && savedCredit != null && savedCredit.getId() != null) {
                try {
                    echeanceService.generatePaymentSchedule(savedCredit);
                } catch (Exception e) {
                    System.err.println("Erreur echeancier: " + e.getMessage());
                }
            }
        }

        return savedCredit;
    }

    public List<Credit> findCreditsByPersonneId(int personneId) throws SQLException {
        return creditRepository.findByPersonneId(personneId);
    }

    public Optional<Credit> findCreditById(int creditId) throws SQLException {
        return creditRepository.findById(creditId);
    }

    public List<Credit> findAllCredits() throws SQLException {
        return creditRepository.findAll();
    }

    public Credit updateCredit(Credit credit) throws SQLException {
        return creditRepository.update(credit);
    }

    public boolean deleteCredit(int creditId) throws SQLException {
        return creditRepository.deleteById(creditId);
    }

    public boolean isExistingClient(int personneId) throws SQLException {
        return creditRepository.hasExistingCredits(personneId);
    }
}
