package view;

import model.entities.Employe;
import model.enums.SecteurEmploi;
import service.EmployeService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class EmployeView {

    private EmployeService employeService = new EmployeService();
    private Scanner sc = new Scanner(System.in);

    public void afficherMenu() {
        int choix;
        do {
            System.out.println("\n===== MENU EMPLOYÉS =====");
            System.out.println("1. Ajouter un employé");
            System.out.println("2. Modifier un employé");
            System.out.println("3. Supprimer un employé");
            System.out.println("4. Lister les employés");
            System.out.println("0. Quitter");
            System.out.print("Votre choix : ");
            choix = sc.nextInt();
            sc.nextLine();

            switch (choix) {
                case 1 -> ajouterEmploye();
                case 2 -> modifierEmploye();
                case 3 -> supprimerEmploye();
                case 4 -> listerEmployes();
                case 0 -> System.out.println("Retour au menu principal...");
                default -> System.out.println("⚠ Choix invalide !");
            }
        } while (choix != 0);
    }

    private void ajouterEmploye() {
        System.out.print("Nom : ");
        String nom = sc.nextLine();
        System.out.print("Prénom : ");
        String prenom = sc.nextLine();
        System.out.print("Ville : ");
        String ville = sc.nextLine();



        System.out.print("Date de naissance (AAAA-MM-JJ) : ");
        LocalDate dateNaissance = LocalDate.parse(sc.nextLine());

        System.out.print("Nombre d’enfants : ");
        int enfants = sc.nextInt();
        System.out.print("Investissement : ");
        double investissement = sc.nextDouble();
        System.out.print("Placement : ");
        double placement = sc.nextDouble();
        sc.nextLine();

        System.out.print("Situation familiale : ");
        String situation = sc.nextLine();

        System.out.print("Salaire : ");
        double salaire = sc.nextDouble();
        System.out.print("Ancienneté (mois) : ");
        int anciennete = sc.nextInt();
        sc.nextLine();

        System.out.print("Secteur (PUBLIC, GRANDE_ENTREPRISE, PME...) : ");
        SecteurEmploi secteur = SecteurEmploi.valueOf(sc.nextLine().toUpperCase());


        System.out.print("Poste : ");
        String poste = sc.nextLine();
        System.out.print("Type contrat : ");
        String contrat = sc.nextLine();



        employeService.ajouterEmploye(nom, prenom, dateNaissance, ville, enfants,
                investissement, placement, situation, LocalDate.now(), 80,
                salaire, anciennete, poste, contrat, secteur);
    }

    private void modifierEmploye() {
        System.out.print("ID de l’employé à modifier : ");
        int id = sc.nextInt();
        sc.nextLine();

        // Ici on peut améliorer avec récupération avant modification
        System.out.println("⚠ Simplifié : entrez les nouvelles infos.");
        // Pour l’exemple, on réutilise ajouterEmploye() mais en fixant l’ID
        ajouterEmploye(); // idée : récupérer un Employe existant et setIdPersonne(id)
    }

    private void supprimerEmploye() {
        System.out.print("ID de l’employé à supprimer : ");
        int id = sc.nextInt();
        employeService.supprimerEmploye(id);
    }

    private void listerEmployes() {
        List<Employe> list = employeService.listerEmployes();
        System.out.println("\n--- Liste des employés ---");
        for (Employe e : list) {
            System.out.println("ID: " + e.getIdPersonne() + " | Nom: " + e.getNom() + " " + e.getPrenom() +
                    " | Poste: " + e.getPoste() + " | Salaire: " + e.getSalaire());
        }
    }
}

