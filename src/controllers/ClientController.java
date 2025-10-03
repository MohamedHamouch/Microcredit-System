package controllers;

import entities.enums.*;
import entities.models.Employe;
import entities.models.Personne;
import entities.models.Professionnel;
import helpers.InputHelper;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import services.ClientService;

public class ClientController {
    
    private ClientService clientService;
    
    public ClientController() {
        this.clientService = new ClientService();
    }
    
    public void createNewClient() {
        try {
            System.out.println("\n=== Créer un nouveau client ===");
            
            System.out.println("Type de client:");
            System.out.println("1. Employé");
            System.out.println("2. Professionnel");
            int clientType = InputHelper.getUserChoice("Choisissez le type (1-2): ", 1, 2);
            
            if (clientType == 1) {
                Employe employe = createEmployeData();
                Employe created = clientService.createEmployeClient(employe);
                System.out.println("Client employé créé avec succès! ID: " + created.getId() + ", Score: " + created.getScore());
            } else {
                Professionnel professionnel = createProfessionnelData();
                Professionnel created = clientService.createProfessionnelClient(professionnel);
                System.out.println("Client professionnel créé avec succès! ID: " + created.getId() + ", Score: " + created.getScore());
            }
            
        } catch (SQLException e) {
            System.out.println("Erreur lors de la création du client: " + e.getMessage());
        }
    }
    
    public void updateClientInfo() {
        try {
            System.out.println("\n=== Modifier informations client ===");
            
            int personneId = InputHelper.getInt("ID du client à modifier: ");
            Optional<Personne> personneOpt = clientService.findPersonneById(personneId);
            
            if (!personneOpt.isPresent()) {
                System.out.println("Client non trouvé.");
                return;
            }
            
            Personne personne = personneOpt.get();
            
            Optional<Employe> employe = clientService.findEmployeByPersonneId(personneId);
            Optional<Professionnel> professionnel = clientService.findProfessionnelByPersonneId(personneId);
            
            if (employe.isPresent()) {
                updateEmployeClient(personne, employe.get());
            } else if (professionnel.isPresent()) {
                updateProfessionnelClient(personne, professionnel.get());
            } else {
                System.out.println("Type de client non déterminé.");
            }
            
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification: " + e.getMessage());
        }
    }
    
    public void viewClientProfile() {
        try {
            System.out.println("\n=== Consulter profil client ===");
            
            int personneId = InputHelper.getPositiveInt("ID du client: ");
            Optional<Personne> personneOpt = clientService.findPersonneById(personneId);
            
            if (!personneOpt.isPresent()) {
                System.out.println("Client non trouvé.");
                return;
            }
            
            Personne personne = personneOpt.get();
            System.out.println(personne);
            
        } catch (SQLException e) {
            System.out.println("Erreur lors de la consultation: " + e.getMessage());
        }
    }
    
    public void deleteClient() {
        try {
            System.out.println("\n=== Supprimer client ===");
            
            int personneId = InputHelper.getInt("ID du client à supprimer: ");
            
            System.out.print("Êtes-vous sûr de vouloir supprimer ce client? (oui/non): ");
            String confirmation = InputHelper.getString("");
            
            if (confirmation.equalsIgnoreCase("oui")) {
                boolean deleted = clientService.deleteClient(personneId);
                if (deleted) {
                    System.out.println("Client supprimé avec succès.");
                } else {
                    System.out.println("Client non trouvé.");
                }
            } else {
                System.out.println("Suppression annulée.");
            }
            
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression: " + e.getMessage());
        }
    }
    
    public void listAllClients() {
        try {
            System.out.println("\n=== Liste de tous les clients ===");
            
            List<Personne> personnes = clientService.findAllPersonnes();
            
            if (personnes.isEmpty()) {
                System.out.println("Aucun client trouvé.");
                return;
            }
            
            System.out.printf("%-5s %-15s %-15s %-12s %-8s %-15s%n", 
                            "ID", "Nom", "Prénom", "Date Nais.", "Score", "Type");
            System.out.println("--------------------------------------------------------------------");
            
            for (Personne personne : personnes) {
                Optional<Employe> employe = clientService.findEmployeByPersonneId(personne.getId());
                Optional<Professionnel> professionnel = clientService.findProfessionnelByPersonneId(personne.getId());
                
                String type = "Inconnu";
                if (employe.isPresent()) {
                    type = "Employé";
                } else if (professionnel.isPresent()) {
                    type = "Professionnel";
                }
                
                System.out.printf("%-5d %-15s %-15s %-12s %-8d %-15s%n",
                                personne.getId(),
                                personne.getNom(),
                                personne.getPrenom(),
                                personne.getDateDeNaissance(),
                                personne.getScore(),
                                type);
            }
            
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des clients: " + e.getMessage());
        }
    }
    

    
    private Employe createEmployeData() {
        Employe employe = new Employe();
        
        fillPersonneData(employe);
        
        employe.setSalaire(InputHelper.getPositiveDouble("Salaire mensuel (DH): "));
        employe.setAnciennete(InputHelper.getInt("Ancienneté (années): "));
        employe.setPoste(InputHelper.getString("Poste: "));
        
        System.out.println("Type de contrat:");
        for (TypeContrat tc : TypeContrat.values()) {
            System.out.println((tc.ordinal() + 1) + ". " + tc.getDescription());
        }
        int tcChoice = InputHelper.getUserChoice("Choisissez (1-" + TypeContrat.values().length + "): ", 1, TypeContrat.values().length);
        employe.setTypeContrat(TypeContrat.values()[tcChoice - 1]);
        
        return employe;
    }
    
    private void fillPersonneData(Personne personne) {
        personne.setNom(InputHelper.getString("Nom: "));
        personne.setPrenom(InputHelper.getString("Prénom: "));
        
        // Date input
        String dateStr = InputHelper.getDateInput("Date de naissance");
        personne.setDateDeNaissance(LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        
        personne.setVille(InputHelper.getString("Ville: "));
        personne.setNombreEnfants(InputHelper.getInt("Nombre d'enfants: "));
        personne.setInvestissement(InputHelper.getBoolean("Avez-vous des investissements?"));
        personne.setPlacement(InputHelper.getBoolean("Avez-vous des placements?"));
        
        // Situation familiale
        System.out.println("Situation familiale:");
        for (SituationFamiliale sf : SituationFamiliale.values()) {
            System.out.println((sf.ordinal() + 1) + ". " + sf.getLabel());
        }
        int sfChoice = InputHelper.getUserChoice("Choisissez (1-" + SituationFamiliale.values().length + "): ", 1, SituationFamiliale.values().length);
        personne.setSituationFamiliale(SituationFamiliale.values()[sfChoice - 1]);
        
        personne.setCreatedAt(LocalDateTime.now());
    }
    
    private Professionnel createProfessionnelData() {
        Professionnel professionnel = new Professionnel();
        
        fillPersonneData(professionnel);
        
        professionnel.setRevenu(InputHelper.getPositiveDouble("Revenu mensuel (DH): "));
        professionnel.setImmatriculationFiscale(InputHelper.getString("Immatriculation fiscale: "));
        professionnel.setActivite(InputHelper.getString("Activité: "));
        
        System.out.println("Secteur d'activité:");
        String[] sectors = {"Agriculture", "Service", "Commerce", "Construction", "Industrie", "Autre"};
        for (int i = 0; i < sectors.length; i++) {
            System.out.println((i + 1) + ". " + sectors[i]);
        }
        int saChoice = InputHelper.getUserChoice("Choisissez (1-" + sectors.length + "): ", 1, sectors.length);
        String secteur = (saChoice == sectors.length) 
            ? InputHelper.getString("Entrez le secteur d'activité: ")
            : sectors[saChoice - 1];
        professionnel.setSecteurActivite(secteur);
        
        System.out.println("Type de contrat:");
        TypeContrat[] professionalContracts = {TypeContrat.PROFESSION_LIBERALE_STABLE, TypeContrat.AUTO_ENTREPRENEUR};
        for (int i = 0; i < professionalContracts.length; i++) {
            System.out.println((i + 1) + ". " + professionalContracts[i].getDescription());
        }
        int tcChoice = InputHelper.getUserChoice("Choisissez (1-" + professionalContracts.length + "): ", 1, professionalContracts.length);
        professionnel.setTypeContrat(professionalContracts[tcChoice - 1]);
        
        return professionnel;
    }
    
    private void updateEmployeClient(Personne personne, Employe employe) throws SQLException {
        System.out.println("Modification des informations (appuyez sur Entrée pour garder la valeur actuelle):");
        
        // Update personne data directly on the employe object (since Employe extends Personne)
        updatePersonneData(employe);
        
        // Update employe data
        System.out.print("Salaire actuel: " + employe.getSalaire() + " DH. Nouveau salaire: ");
        String newSalaire = InputHelper.getOptionalString("");
        if (!newSalaire.isEmpty()) {
            employe.setSalaire(Double.parseDouble(newSalaire));
        }
        
        Employe updated = clientService.updateEmployeClient(employe);
        System.out.println("Client employé modifié avec succès! Nouveau score: " + updated.getScore());
    }
    
    private void updateProfessionnelClient(Personne personne, Professionnel professionnel) throws SQLException {
        System.out.println("Modification des informations (appuyez sur Entrée pour garder la valeur actuelle):");
        
        // Update personne data directly on the professionnel object (since Professionnel extends Personne)
        updatePersonneData(professionnel);
        
        // Update professionnel data
        System.out.print("Revenu actuel: " + professionnel.getRevenu() + " DH. Nouveau revenu: ");
        String newRevenu = InputHelper.getOptionalString("");
        if (!newRevenu.isEmpty()) {
            professionnel.setRevenu(Double.parseDouble(newRevenu));
        }
        
        Professionnel updated = clientService.updateProfessionnelClient(professionnel);
        System.out.println("Client professionnel modifié avec succès! Nouveau score: " + updated.getScore());
    }
    
    private void updatePersonneData(Personne personne) {
        System.out.print("Nom actuel: " + personne.getNom() + ". Nouveau nom: ");
        String newNom = InputHelper.getOptionalString("");
        if (!newNom.isEmpty()) {
            personne.setNom(newNom);
        }
        
        System.out.print("Prénom actuel: " + personne.getPrenom() + ". Nouveau prénom: ");
        String newPrenom = InputHelper.getOptionalString("");
        if (!newPrenom.isEmpty()) {
            personne.setPrenom(newPrenom);
        }
    }
}