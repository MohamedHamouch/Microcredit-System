package views;

import controllers.EcheanceController;
import helpers.InputHelper;

public class EcheanceView {
    
    private EcheanceController echeanceController;

    public EcheanceView() {
        this.echeanceController = new EcheanceController();
    }
    
    public void showEcheanceMenu() {
        while (true) {
            System.out.println("\n==================================================");
            System.out.println("         GESTION DES ECHEANCES");
            System.out.println("==================================================");
            System.out.println("1. Enregistrer paiement");
            System.out.println("2. Verifier et mettre a jour les statuts");
            System.out.println("0. Retour au menu principal");
            System.out.println("==================================================");
            
            int choice = InputHelper.getUserChoice("Votre choix: ", 0, 2);
            
            switch (choice) {
                case 1:
                    echeanceController.recordPayment();
                    break;
                case 2:
                    echeanceController.checkAndUpdateStatuses();
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