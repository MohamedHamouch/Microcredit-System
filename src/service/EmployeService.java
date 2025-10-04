package service;

import dao.DatabaseConnection;
import dao.EmployeDAO;
import model.entities.Employe;
import model.enums.SecteurEmploi;

import java.sql.SQLException;
import java.util.List;

public class EmployeService {

    private EmployeDAO employeDAO;

    public EmployeService() {
        // ✅ Utilisation de la connexion de DatabaseConnection
        this.employeDAO = new EmployeDAO(DatabaseConnection.getInstance().getConnection());
    }


    public void ajouterEmploye(String nom, String prenom,
                               java.time.LocalDate dateNaissance, String ville,
                               int nombreEnfants, double investissement, double placement,
                               String situationFamiliale, java.time.LocalDate createdAt, double score,
                               double salaire, int anciennete, String poste, String typeContrat,
                               SecteurEmploi secteur) {

        Employe e = new Employe(nom, prenom, dateNaissance, ville,
                nombreEnfants, investissement, placement, situationFamiliale,
                createdAt, score, salaire, anciennete, poste, typeContrat, secteur);

        try {
            employeDAO.ajouterEmploye(e);
        } catch (SQLException ex) {
            System.out.println("❌ Erreur lors de l'ajout de l'employé : " + ex.getMessage());
        }
    }


    public void modifierEmploye(Employe e) {
        try {
            employeDAO.modifierEmploye(e);
        } catch (SQLException ex) {
            System.out.println("❌ Erreur lors de la modification de l'employé : " + ex.getMessage());
        }
    }


    public void supprimerEmploye(int idPersonne) {
        try {
            employeDAO.supprimerEmploye(idPersonne);
        } catch (SQLException ex) {
            System.out.println("❌ Erreur lors de la suppression de l'employé : " + ex.getMessage());
        }
    }


    public List<Employe> listerEmployes() {
        try {
            return employeDAO.listerEmployes();
        } catch (SQLException ex) {
            System.out.println("❌ Erreur lors du listing des employés : " + ex.getMessage());
            return List.of();
        }
    }
}

