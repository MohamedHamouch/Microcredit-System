package repository;

import config.db.DatabaseConnection;
import entities.enums.TypeContrat;
import entities.models.Personne;
import entities.models.Professionnel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProfessionnelRepository {

    public Professionnel create(Professionnel professionnel) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        try {
            connection.setAutoCommit(false);
            
            PersonneRepository personneRepository = new PersonneRepository();
            Personne createdPersonne = personneRepository.savePersonneBase(professionnel);
            professionnel.setId(createdPersonne.getId());
            professionnel.setCreatedAt(createdPersonne.getCreatedAt());
            
            String sql = "INSERT INTO professionnel (personne_id, revenu, immatriculation_fiscale, secteur_activite, activite, type_contrat) VALUES (?, ?, ?, ?, ?, ?::typecontrat_enum)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, professionnel.getId());
                stmt.setDouble(2, professionnel.getRevenu());
                stmt.setString(3, professionnel.getImmatriculationFiscale());
                stmt.setString(4, professionnel.getSecteurActivite());
                stmt.setString(5, professionnel.getActivite());
                stmt.setString(6, professionnel.getTypeContrat().name());
                
                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating professionnel failed, no rows affected.");
                }
            }
            
            connection.commit();
            return professionnel;
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
    }

    public Optional<Professionnel> findByPersonneId(int personneId) throws SQLException {
        String sql = "SELECT p.*, pr.revenu, pr.immatriculation_fiscale, pr.secteur_activite, pr.activite, pr.type_contrat " +
                    "FROM personne p JOIN professionnel pr ON p.id = pr.personne_id WHERE p.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, personneId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToProfessionnel(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Professionnel> findAll() throws SQLException {
        List<Professionnel> out = new ArrayList<>();
        String sql = "SELECT * FROM professionnel";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Professionnel p = new Professionnel();
                p.setRevenu(rs.getDouble("revenu"));
                p.setImmatriculationFiscale(rs.getString("immatriculation_fiscale"));
                p.setSecteurActivite(rs.getString("secteur_activite"));
                p.setActivite(rs.getString("activite"));
                p.setTypeContrat(TypeContrat.valueOf(rs.getString("type_contrat")));
                out.add(p);
            }
        }
        return out;
    }

    public Professionnel update(Professionnel professionnel) throws SQLException {
        PersonneRepository personneRepository = new PersonneRepository();
        personneRepository.updatePersonneBase(professionnel);
        
        String sql = "UPDATE professionnel SET revenu = ?, immatriculation_fiscale = ?, secteur_activite = ?, activite = ?, type_contrat = ?::typecontrat_enum WHERE personne_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setDouble(1, professionnel.getRevenu());
            stmt.setString(2, professionnel.getImmatriculationFiscale());
            stmt.setString(3, professionnel.getSecteurActivite());
            stmt.setString(4, professionnel.getActivite());
            stmt.setString(5, professionnel.getTypeContrat().name());
            stmt.setInt(6, professionnel.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating professionnel failed, no rows affected.");
            }
        }
        return professionnel;
    }

    public boolean deleteByPersonneId(int personneId) throws SQLException {
        String sql = "DELETE FROM professionnel WHERE personne_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, personneId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }
    
    private Professionnel mapResultSetToProfessionnel(ResultSet rs) throws SQLException {
        Professionnel professionnel = new Professionnel();
        
        professionnel.setId(rs.getInt("id"));
        professionnel.setNom(rs.getString("nom"));
        professionnel.setPrenom(rs.getString("prenom"));
        
        Date dateNaissance = rs.getDate("date_de_naissance");
        if (dateNaissance != null) {
            professionnel.setDateDeNaissance(dateNaissance.toLocalDate());
        }
        
        professionnel.setVille(rs.getString("ville"));
        professionnel.setNombreEnfants(rs.getInt("nombre_enfants"));
        professionnel.setInvestissement(rs.getBoolean("investissement"));
        professionnel.setPlacement(rs.getBoolean("placement"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            professionnel.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        professionnel.setScore(rs.getInt("score"));
        
        String situationFamiliale = rs.getString("situation_familiale");
        if (situationFamiliale != null && !situationFamiliale.isEmpty()) {
            try {
                professionnel.setSituationFamiliale(entities.enums.SituationFamiliale.valueOf(situationFamiliale));
            } catch (IllegalArgumentException e) {
            }
        }
        
        professionnel.setRevenu(rs.getDouble("revenu"));
        professionnel.setImmatriculationFiscale(rs.getString("immatriculation_fiscale"));
        professionnel.setSecteurActivite(rs.getString("secteur_activite"));
        professionnel.setActivite(rs.getString("activite"));
        
        String typeContrat = rs.getString("type_contrat");
        if (typeContrat != null && !typeContrat.isEmpty()) {
            try {
                professionnel.setTypeContrat(TypeContrat.valueOf(typeContrat));
            } catch (IllegalArgumentException e) {
            }
        }
        
        return professionnel;
    }
}
