package controllers;

import entities.enums.TypeCredit;
import entities.models.Credit;
import entities.models.Employe;
import entities.models.Personne;
import entities.models.Professionnel;
import helpers.InputHelper;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import services.ClientService;
import services.CreditService;

public class CreditController {
    
    private CreditService creditService;
    private ClientService clientService;
    
    public CreditController() {
        this.creditService = new CreditService();
        this.clientService = new ClientService();
    }
    
    public void requestCredit() {
        try {
            System.out.println("\n=== DEMANDE DE CRÉDIT ===");
            
            int personneId = InputHelper.getPositiveInt("ID du client: ");
            Optional<Personne> personneOpt = clientService.findPersonneById(personneId);
            
            if (!personneOpt.isPresent()) {
                System.out.println("Client non trouvé.");
                return;
            }
            
            Personne personne = personneOpt.get();
            System.out.println("Client: " + personne.getPrenom() + " " + personne.getNom());
            System.out.println("Score actuel: " + personne.getScore());
            
            boolean isExisting = creditService.isExistingClient(personneId);
            System.out.println("Type de client: " + (isExisting ? "Existant" : "Nouveau"));
            
            double montantDemande = InputHelper.getPositiveDouble("Montant demandé (DH): ");
            
            System.out.println("\nTypes de crédit disponibles:");
            TypeCredit[] types = TypeCredit.values();
            for (int i = 0; i < types.length; i++) {
                System.out.println((i + 1) + ". " + types[i].getLabel());
            }
            int typeChoice = InputHelper.getUserChoice("Choisissez le type (1-" + types.length + "): ", 1, types.length);
            TypeCredit typeCredit = types[typeChoice - 1];
            
            int dureeEnMois = InputHelper.getPositiveInt("Durée en mois: ");
            double tauxInteret = InputHelper.getPercentage("Taux d'intérêt (0-5%): ", 0.0, 5.0) / 100.0;
            
            Credit credit = creditService.processCreditRequest(
                personneId, montantDemande, tauxInteret, typeCredit, dureeEnMois
            );
            
            System.out.println("Demande: " + String.format("%.2f DH", credit.getMontantDemande()));
            System.out.println("Octroye: " + String.format("%.2f DH", credit.getMontantOctroye()));
            System.out.println("Decision: " + credit.getDecision().getMessage());
            switch (credit.getDecision()) {
                case ACCORD_IMMEDIAT:
                    System.out.println("APPROUVE");
                    break;
                case ETUDE_MANUELLE:
                    System.out.println("ETUDE MANUELLE");
                    break;
                case REFUS_AUTOMATIQUE:
                    System.out.println("REFUSE");
                    break;
            }
            
        } catch (SQLException e) {
            System.out.println("Erreur lors du traitement de la demande: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
    
    public void viewClientCredits() {
        try {
            System.out.println("\n=== HISTORIQUE DES CRÉDITS CLIENT ===");
            
            int personneId = InputHelper.getPositiveInt("ID du client: ");
            Optional<Personne> personneOpt = clientService.findPersonneById(personneId);
            
            if (!personneOpt.isPresent()) {
                System.out.println("Client non trouvé.");
                return;
            }
            
            Personne personne = personneOpt.get();
            System.out.println("Client: " + personne.getPrenom() + " " + personne.getNom());
            
            List<Credit> credits = creditService.findCreditsByPersonneId(personneId);
            
            if (credits.isEmpty()) {
                System.out.println("Aucun crédit trouvé pour ce client.");
                return;
            }
            
            System.out.println("\n" + credits.size() + " crédit(s) trouvé(s):");
            System.out.println("-----------------------------------------------------------");
            System.out.printf("%-5s %-12s %-15s %-15s %-10s %-15s%n", 
                            "ID", "Date", "Montant Dem.", "Montant Oct.", "Durée", "Décision");
            System.out.println("-----------------------------------------------------------");
            
            for (Credit credit : credits) {
                System.out.printf("%-5d %-12s %-15.2f %-15.2f %-10d %-15s%n",
                                credit.getId(),
                                credit.getDateCredit(),
                                credit.getMontantDemande(),
                                credit.getMontantOctroye(),
                                credit.getDureeEnMois(),
                                credit.getDecision().getMessage());
            }
            
        } catch (SQLException e) {
            System.out.println("Erreur lors de la consultation: " + e.getMessage());
        }
    }
    
    public void viewAllCredits() {
        try {
            System.out.println("\n=== TOUS LES CRÉDITS ===");
            
            List<Credit> credits = creditService.findAllCredits();
            
            if (credits.isEmpty()) {
                System.out.println("Aucun crédit trouvé.");
                return;
            }
            
            System.out.println(credits.size() + " crédit(s) au total:");
            System.out.println("-----------------------------------------------------------------------");
            System.out.printf("%-5s %-10s %-12s %-15s %-15s %-10s %-15s%n", 
                            "ID", "Client", "Date", "Montant Dem.", "Montant Oct.", "Durée", "Décision");
            System.out.println("-----------------------------------------------------------------------");
            
            for (Credit credit : credits) {
                System.out.printf("%-5d %-10d %-12s %-15.2f %-15.2f %-10d %-15s%n",
                                credit.getId(),
                                credit.getPersonneId(),
                                credit.getDateCredit(),
                                credit.getMontantDemande(),
                                credit.getMontantOctroye(),
                                credit.getDureeEnMois(),
                                credit.getDecision().getMessage());
            }
            
        } catch (SQLException e) {
            System.out.println("Erreur lors de la consultation: " + e.getMessage());
        }
    }
    
    public void checkBorrowingCapacity() {
        try {
            System.out.println("\n=== VÉRIFICATION DE LA CAPACITÉ D'EMPRUNT ===");
            
            int personneId = InputHelper.getPositiveInt("ID du client: ");
            Optional<Personne> personneOpt = clientService.findPersonneById(personneId);
            
            if (!personneOpt.isPresent()) {
                System.out.println("Client non trouvé.");
                return;
            }
            
            Personne personne = personneOpt.get();
            boolean isExisting = creditService.isExistingClient(personneId);
            int score = personne.getScore();
            
            System.out.println("\nClient: " + personne.getPrenom() + " " + personne.getNom());
            System.out.println("Score: " + score);
            System.out.println("Type: " + (isExisting ? "Client existant" : "Nouveau client"));
            
            String decisionCategory;
            if (score >= 80) {
                decisionCategory = "ACCORD IMMEDIAT";
            } else if ((isExisting && score >= 50) || (!isExisting && score >= 60)) {
                decisionCategory = "ETUDE MANUELLE";
            } else {
                decisionCategory = "REFUS AUTOMATIQUE";
            }
            
            System.out.println("Decision: " + decisionCategory);
            
            double monthlyIncome = clientService.getMonthlyIncome(personne);
            double capacity;
            if (!isExisting) {
                capacity = monthlyIncome * 4;
            } else {
                if (score > 80) {
                    capacity = monthlyIncome * 10;
                } else {
                    capacity = monthlyIncome * 7;
                }
            }
            
            System.out.println("Revenu mensuel: " + String.format("%.2f DH", monthlyIncome));
            System.out.println("Capacite d'emprunt: " + String.format("%.2f DH", capacity));
            
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification: " + e.getMessage());
        }
    }

}