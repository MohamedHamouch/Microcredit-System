package views;

import controllers.CreditController;
import helpers.InputHelper;

public class CreditView {
    
    private CreditController creditController;
    
    public CreditView() {
        this.creditController = new CreditController();
    }
    
    public void showCreditMenu() {
        while (true) {
            System.out.println("\n==================================================");
            System.out.println("         GESTION DES CRÉDITS");
            System.out.println("==================================================");
            System.out.println("1. Faire une demande de crédit");
            System.out.println("2. Consulter historique crédits client");
            System.out.println("3. Vérifier capacité d'emprunt");
            System.out.println("4. Voir tous les crédits");
            System.out.println("0. Retour au menu principal");
            System.out.println("==================================================");
            
            int choice = InputHelper.getUserChoice("Votre choix: ", 0, 4);
            
            switch (choice) {
                case 1:
                    creditController.requestCredit();
                    break;
                case 2:
                    creditController.viewClientCredits();
                    break;
                case 3:
                    creditController.checkBorrowingCapacity();
                    break;
                case 4:
                    creditController.viewAllCredits();
                    break;
                case 0:
                    System.out.println("Retour au menu principal...");
                    return;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }
}