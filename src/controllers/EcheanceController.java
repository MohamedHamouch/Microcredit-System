package controllers;

import entities.models.Credit;
import entities.models.Echeance;
import helpers.InputHelper;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import services.CreditService;
import services.EcheanceService;

public class EcheanceController {
    
    private final EcheanceService echeanceService;
    private final CreditService creditService;
    
    public EcheanceController() {
        this.echeanceService = new EcheanceService();
        this.creditService = new CreditService();
    }
    
    public void recordPayment() {
        try {
            System.out.println("\n=== ENREGISTRER PAIEMENT ===");
            
            int creditId = InputHelper.getPositiveInt("ID du credit: ");
            
            Optional<Credit> creditOpt = creditService.findCreditById(creditId);
            if (!creditOpt.isPresent()) {
                System.out.println("Credit non trouve.");
                return;
            }
            
            Credit credit = creditOpt.get();
            System.out.println("Credit: " + credit.getMontantOctroye() + " DH");
            
            List<Echeance> unpaidEcheances = echeanceService.getUnpaidEcheancesByCreditId(creditId);
            
            if (unpaidEcheances.isEmpty()) {
                System.out.println("Aucune echeance impayee pour ce credit.");
                return;
            }
            
            System.out.println("\nEcheances impayees:");
            for (int i = 0; i < unpaidEcheances.size(); i++) {
                Echeance e = unpaidEcheances.get(i);
                System.out.println((i+1) + ". Date: " + e.getDateEcheance() + 
                                 " - Montant: " + e.getMensualite() + " DH");
            }
            
            int choice = InputHelper.getUserChoice("Choisissez l'echeance (1-" + unpaidEcheances.size() + "): ", 
                                                   1, unpaidEcheances.size());
            
            Echeance selectedEcheance = unpaidEcheances.get(choice - 1);
            
            LocalDate paymentDate = LocalDate.now();            
            echeanceService.recordPayment(selectedEcheance.getId(), paymentDate);
            
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'enregistrement: " + e.getMessage());
        }
    }
    
    public void checkAndUpdateStatuses() {
        try {
            System.out.println("\n=== VERIFICATION DES ECHEANCES ===");
            echeanceService.updateEcheanceStatuses();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la verification: " + e.getMessage());
        }
    }
}
