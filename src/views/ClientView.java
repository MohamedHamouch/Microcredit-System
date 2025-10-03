package views;

import controllers.ClientController;
import helpers.InputHelper;

public class ClientView {
    
    private ClientController clientController;
    
    public ClientView() {
        this.clientController = new ClientController();
    }
    
    public void showClientMenu() {
        while (true) {
            System.out.println("\n==================================================");
            System.out.println("           GESTION DES CLIENTS");
            System.out.println("==================================================");
            System.out.println("1. Créer un nouveau client");
            System.out.println("2. Modifier informations client");
            System.out.println("3. Consulter profil client");
            System.out.println("4. Supprimer client");
            System.out.println("5. Lister tous les clients");
            System.out.println("0. Retour au menu principal");
            System.out.println("==================================================");
            
            int choice = InputHelper.getUserChoice("Votre choix: ", 0, 5);
            
            switch (choice) {
                case 1:
                    clientController.createNewClient();
                    break;
                case 2:
                    clientController.updateClientInfo();
                    break;
                case 3:
                    clientController.viewClientProfile();
                    break;
                case 4:
                    clientController.deleteClient();
                    break;
                case 5:
                    clientController.listAllClients();
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