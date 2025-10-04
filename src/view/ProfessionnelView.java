package view;

import model.entities.Professionnel;
import model.enums.SecteurActivite;
import service.ProfessionnelService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ProfessionnelView {

    private ProfessionnelService professionnelService = new ProfessionnelService();
    private Scanner sc = new Scanner(System.in);

    public void afficherMenu() {
        int choix;
        do {
            System.out.println("\n===== MENU PROFESSIONNELS =====");
            System.out.println("1. Ajouter un professionnel");
            System.out.println("2. Modifier un professionnel");
            System.out.println("3. Supprimer un professionnel");
            System.out.println("4. Lister les professionnels");
            System.out.println("0. Quitter");
            System.out.print("Votre choix : ");
            choix = sc.nextInt();
            sc.nextLine();

            switch (choix) {
                case 1 -> ajouterProfessionnel();
                case 2 -> modifierProfessionnel();
                case 3 -> supprimerProfessionnel();
                case 4 -> listerProfessionnels();
                case 0 -> System.out.println("Retour au menu principal...");
                default -> System.out.println("⚠ Choix invalide !");
            }
        } while (choix != 0);
    }

    private void ajouterProfessionnel() {
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

        System.out.print("Revenu : ");
        double revenu = sc.nextDouble();
        sc.nextLine();

        System.out.print("Immatriculation fiscale : ");
        String fiscal = sc.nextLine();

        System.out.print("Secteur (SERVICE, COMMERCE, INDUSTRIE...) : ");
        SecteurActivite secteur = SecteurActivite.valueOf(sc.nextLine().toUpperCase());

        System.out.print("Activité : ");
        String activite = sc.nextLine();

        professionnelService.ajouterProfessionnel(nom, prenom, dateNaissance, ville, enfants,
                investissement, placement, situation, LocalDate.now(), 90,
                revenu, fiscal, secteur, activite);
    }

    private void modifierProfessionnel() {
        System.out.print("ID du professionnel à modifier : ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.println("⚠ Simplifié : entrez les nouvelles infos.");
        ajouterProfessionnel(); // idem, on réutilise la saisie
    }

    private void supprimerProfessionnel() {
        System.out.print("ID du professionnel à supprimer : ");
        int id = sc.nextInt();
        professionnelService.supprimerProfessionnel(id);
    }

    private void listerProfessionnels() {
        List<Professionnel> list = professionnelService.listerProfessionnels();
        System.out.println("\n--- Liste des professionnels ---");
        for (Professionnel p : list) {
            System.out.println("ID: " + p.getIdPersonne() + " | Nom: " + p.getNom() + " " + p.getPrenom() +
                    " | Activité: " + p.getActivite() + " | Revenu: " + p.getRevenu());
        }
    }
}
