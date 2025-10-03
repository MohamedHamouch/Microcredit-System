package views;

import helpers.InputHelper;

public class MainView {
    
    private ClientView clientView;
    private CreditView creditView;
    private EcheanceView echeanceView;

    public MainView() {
        this.clientView = new ClientView();
        this.creditView = new CreditView();
        this.echeanceView = new EcheanceView();
    }
    
    public void showMainMenu() {
        System.out.println("Bienvenue dans le système de MicroCrédit!");
        
        while (true) {
            System.out.println("\n==================================================");
            System.out.println("         SYSTÈME DE MICROCRÉDIT");
            System.out.println("==================================================");
            System.out.println("1. Gestion des clients");
            System.out.println("2. Gestion des crédits et scoring");
            System.out.println("3. Gestion des écheances");
            System.out.println("4. Rapports");
            System.out.println("0. Quitter");
            System.out.println("==================================================");
            
            int choice = InputHelper.getUserChoice("Votre choix: ", 0, 4);
            
            switch (choice) {
                case 1:
                    clientView.showClientMenu();
                    break;
                case 2:
                    creditView.showCreditMenu();
                    break;
                case 3:
                    echeanceView.showEcheanceMenu();
                    break;
                case 4:
                    System.out.println("Test test test");

                    break;
                case 0:
                    System.out.println("Au revoir!");
                    return;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }

}