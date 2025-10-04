package view;

import service.EmployeService;
import service.ProfessionnelService;

import java.util.Scanner;

public class MenuView {
    public void affichermenu() {
        Scanner sc = new Scanner(System.in);


        ProfessionnelView professionnelview = new ProfessionnelView();
        EmployeView employeview = new EmployeView();

        int choix;
        do {
            System.out.println("\n===== MENU PRINCIPAL =====");
            System.out.println("1. Gestion des Employés");
            System.out.println("2. Gestion des Professionnels");
            System.out.println("0. Quitter");
            System.out.print("👉 Choix : ");
            choix = sc.nextInt();
            sc.nextLine();

            switch (choix) {
                case 1 -> employeview.afficherMenu();
                case 2 -> professionnelview.afficherMenu();
                case 0 -> System.out.println("👋 Fin du programme");
                default -> System.out.println("❌ Choix invalide !");
            }

        } while (choix != 0);

        sc.close();
    }
}


