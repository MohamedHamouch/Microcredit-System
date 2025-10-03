package repository;

import config.db.DatabaseConnection;
import entities.models.Employe;
import entities.models.Personne;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeRepository {

    public Employe create(Employe employe) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            PersonneRepository pr = new PersonneRepository();
            Personne created = pr.savePersonneBase(employe);
            employe.setId(created.getId());
            employe.setCreatedAt(created.getCreatedAt());
            
            String sql = "INSERT INTO employe (personne_id, salaire, anciennete, poste, type_contrat) VALUES (?,?,?,?,?::typecontrat_enum)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, employe.getId());
                stmt.setDouble(2, employe.getSalaire());
                stmt.setInt(3, employe.getAnciennete());
                stmt.setString(4, employe.getPoste());
                stmt.setString(5, employe.getTypeContrat().name());
                stmt.executeUpdate();
            }
            conn.commit();
            return employe;
        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    public Optional<Employe> findByPersonneId(int personneId) throws SQLException {
        String sql = "SELECT p.*, e.salaire, e.anciennete, e.poste, e.type_contrat " +
                    "FROM personne p JOIN employe e ON p.id = e.personne_id WHERE p.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, personneId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEmploye(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Employe> findAll() throws SQLException {
        List<Employe> out = new ArrayList<>();
        String sql = "SELECT * FROM employe";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Employe emp = new Employe();
                emp.setSalaire(rs.getDouble("salaire"));
                emp.setAnciennete(rs.getInt("anciennete"));
                emp.setPoste(rs.getString("poste"));
                String tc = rs.getString("type_contrat"); if (tc!=null) try { emp.setTypeContrat(entities.enums.TypeContrat.valueOf(tc)); } catch(Exception ignore) {}
                out.add(emp);
            }
        }
        return out;
    }

    public Employe update(Employe employe) throws SQLException {
        PersonneRepository pr = new PersonneRepository();
        pr.updatePersonneBase(employe);
        
        String sql = "UPDATE employe SET salaire=?, anciennete=?, poste=?, type_contrat=?::typecontrat_enum WHERE personne_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, employe.getSalaire());
            stmt.setInt(2, employe.getAnciennete());
            stmt.setString(3, employe.getPoste());
            stmt.setString(4, employe.getTypeContrat().name());
            stmt.setInt(5, employe.getId());
            stmt.executeUpdate();
        }
        return employe;
    }

    public boolean deleteByPersonneId(int personneId) throws SQLException {
        String sql = "DELETE FROM employe WHERE personne_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, personneId);
            int r = stmt.executeUpdate();
            return r>0;
        }
    }
    
    private Employe mapResultSetToEmploye(ResultSet rs) throws SQLException {
        Employe employe = new Employe();
        
        // Map Personne fields
        employe.setId(rs.getInt("id"));
        employe.setNom(rs.getString("nom"));
        employe.setPrenom(rs.getString("prenom"));
        
        Date dateNaissance = rs.getDate("date_de_naissance");
        if (dateNaissance != null) {
            employe.setDateDeNaissance(dateNaissance.toLocalDate());
        }
        
        employe.setVille(rs.getString("ville"));
        employe.setNombreEnfants(rs.getInt("nombre_enfants"));
        employe.setInvestissement(rs.getBoolean("investissement"));
        employe.setPlacement(rs.getBoolean("placement"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            employe.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        employe.setScore(rs.getInt("score"));
        
        String situationFamiliale = rs.getString("situation_familiale");
        if (situationFamiliale != null && !situationFamiliale.isEmpty()) {
            try {
                employe.setSituationFamiliale(entities.enums.SituationFamiliale.valueOf(situationFamiliale));
            } catch (IllegalArgumentException e) {
            }
        }
        
        employe.setSalaire(rs.getDouble("salaire"));
        employe.setAnciennete(rs.getInt("anciennete"));
        employe.setPoste(rs.getString("poste"));
        
        String typeContrat = rs.getString("type_contrat");
        if (typeContrat != null && !typeContrat.isEmpty()) {
            try {
                employe.setTypeContrat(entities.enums.TypeContrat.valueOf(typeContrat));
            } catch (IllegalArgumentException e) {
            }
        }
        
        return employe;
    }
}
