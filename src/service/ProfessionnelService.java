package service;

import dao.DatabaseConnection;
import dao.EmployeDAO;
import dao.ProfessinalDAO;
import model.entities.Professionnel;
import model.enums.SecteurActivite;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ProfessionnelService {

    private ProfessinalDAO professionnelDAO;

    public ProfessionnelService() {
        this.professionnelDAO = new ProfessinalDAO(DatabaseConnection.getInstance().getConnection());
    }


    public void ajouterProfessionnel(String nom, String prenom, LocalDate dateNaissance, String ville,
                                     int nombreEnfants, double investissement, double placement,
                                     String situationFamiliale, LocalDate createdAt, double score,
                                     double revenu, String immatriculationFiscale,
                                     SecteurActivite secteurActivite, String activite) {

        Professionnel p = new Professionnel(
                nom, prenom, dateNaissance, ville,
                nombreEnfants, investissement, placement,
                situationFamiliale, createdAt, score,
                revenu, immatriculationFiscale, secteurActivite, activite
        );

        try {
            professionnelDAO.ajouterProfessionnel(p);
        } catch (SQLException ex) {
            System.out.println("❌ Erreur lors de l'ajout du professionnel : " + ex.getMessage());
        }
    }


    public void modifierProfessionnel(Professionnel p) {
        try {
            professionnelDAO.modifierProfessionnel(p);
        } catch (SQLException ex) {
            System.out.println("❌ Erreur lors de la modification du professionnel : " + ex.getMessage());
        }
    }


    public void supprimerProfessionnel(int idPersonne) {
        try {
            professionnelDAO.supprimerProfessionel(idPersonne);
        } catch (SQLException ex) {
            System.out.println("❌ Erreur lors de la suppression du professionnel : " + ex.getMessage());
        }
    }


    public List<Professionnel> listerProfessionnels() {
        try {
            return professionnelDAO.listerProfessionnels();
        } catch (SQLException ex) {
            System.out.println("❌ Erreur lors du listing des professionnels : " + ex.getMessage());
            return List.of();
        }
    }
}

