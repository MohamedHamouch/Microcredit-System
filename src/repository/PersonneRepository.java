package repository;

import config.db.DatabaseConnection;
import entities.enums.SituationFamiliale;
import entities.enums.TypeContrat;
import entities.models.Employe;
import entities.models.Personne;
import entities.models.Professionnel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonneRepository {

    public Personne savePersonneBase(Personne personne) throws SQLException {
        String sql = "INSERT INTO personne (nom, prenom, date_de_naissance, ville, nombre_enfants, investissement, placement, situation_familiale, created_at, score) VALUES (?, ?, ?, ?, ?, ?, ?, ?::situation_familiale_enum, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, personne.getNom());
            stmt.setString(2, personne.getPrenom());
            if (personne.getDateDeNaissance() != null) {
                stmt.setDate(3, Date.valueOf(personne.getDateDeNaissance()));
            } else {
                stmt.setNull(3, java.sql.Types.DATE);
            }
            stmt.setString(4, personne.getVille());
            stmt.setInt(5, personne.getNombreEnfants());
            stmt.setBoolean(6, personne.isInvestissement());
            stmt.setBoolean(7, personne.isPlacement());
            if (personne.getSituationFamiliale() != null) {
                stmt.setString(8, personne.getSituationFamiliale().name());
            } else {
                stmt.setNull(8, java.sql.Types.VARCHAR);
            }
            stmt.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
            stmt.setInt(10, personne.getScore());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating personne failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    personne.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating personne failed, no ID obtained.");
                }
            }
        }
        return personne;
    }

    public Optional<Personne> findById(int id) throws SQLException {
        Optional<Employe> employe = findEmployeById(id);
        if (employe.isPresent()) {
            return Optional.of(employe.get());
        }
        
        Optional<Professionnel> professionnel = findProfessionnelById(id);
        if (professionnel.isPresent()) {
            return Optional.of(professionnel.get());
        }
        
        return Optional.empty();
    }
    
    private Optional<Employe> findEmployeById(int id) throws SQLException {
        String sql = "SELECT p.*, e.salaire, e.anciennete, e.poste, e.type_contrat " +
                    "FROM personne p JOIN employe e ON p.id = e.personne_id WHERE p.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEmploye(rs));
                }
            }
        }
        return Optional.empty();
    }
    
    private Optional<Professionnel> findProfessionnelById(int id) throws SQLException {
        String sql = "SELECT p.*, pr.revenu, pr.immatriculation_fiscale, pr.secteur_activite, pr.activite, pr.type_contrat " +
                    "FROM personne p JOIN professionnel pr ON p.id = pr.personne_id WHERE p.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToProfessionnel(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Personne> findAll() throws SQLException {
        List<Personne> personnes = new ArrayList<>();
        
        String employeSQL = "SELECT p.*, e.salaire, e.anciennete, e.poste, e.type_contrat " +
                           "FROM personne p JOIN employe e ON p.id = e.personne_id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(employeSQL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                personnes.add(mapResultSetToEmploye(rs));
            }
        }
        
        String profSQL = "SELECT p.*, pr.revenu, pr.immatriculation_fiscale, pr.secteur_activite, pr.activite, pr.type_contrat " +
                        "FROM personne p JOIN professionnel pr ON p.id = pr.personne_id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(profSQL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                personnes.add(mapResultSetToProfessionnel(rs));
            }
        }
        
        return personnes;
    }

    public Personne updatePersonneBase(Personne personne) throws SQLException {
        String sql = "UPDATE personne SET nom = ?, prenom = ?, date_de_naissance = ?, ville = ?, nombre_enfants = ?, investissement = ?, placement = ?, situation_familiale = ?::situation_familiale_enum, score = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, personne.getNom());
            stmt.setString(2, personne.getPrenom());
            if (personne.getDateDeNaissance() != null) {
                stmt.setDate(3, Date.valueOf(personne.getDateDeNaissance()));
            } else {
                stmt.setNull(3, java.sql.Types.DATE);
            }
            stmt.setString(4, personne.getVille());
            stmt.setInt(5, personne.getNombreEnfants());
            stmt.setBoolean(6, personne.isInvestissement());
            stmt.setBoolean(7, personne.isPlacement());
            if (personne.getSituationFamiliale() != null) {
                stmt.setString(8, personne.getSituationFamiliale().name());
            } else {
                stmt.setNull(8, java.sql.Types.VARCHAR);
            }
            stmt.setInt(9, personne.getScore());
            stmt.setInt(10, personne.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating personne failed, no rows affected.");
            }
        }
        return personne;
    }

    public boolean deleteById(int id) throws SQLException {
        String sql = "DELETE FROM personne WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int affected = stmt.executeUpdate();
            return affected > 0;
        }
    }
    
    private Employe mapResultSetToEmploye(ResultSet rs) throws SQLException {
        Employe employe = new Employe();
        
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
            employe.setSituationFamiliale(SituationFamiliale.valueOf(situationFamiliale));
        }
        
        employe.setSalaire(rs.getDouble("salaire"));
        employe.setAnciennete(rs.getInt("anciennete"));
        employe.setPoste(rs.getString("poste"));
        
        String typeContrat = rs.getString("type_contrat");
        if (typeContrat != null && !typeContrat.isEmpty()) {
            employe.setTypeContrat(TypeContrat.valueOf(typeContrat));
        }
        
        return employe;
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
            professionnel.setSituationFamiliale(SituationFamiliale.valueOf(situationFamiliale));
        }
        
        professionnel.setRevenu(rs.getDouble("revenu"));
        professionnel.setImmatriculationFiscale(rs.getString("immatriculation_fiscale"));
        professionnel.setSecteurActivite(rs.getString("secteur_activite"));
        professionnel.setActivite(rs.getString("activite"));
        
        String typeContrat = rs.getString("type_contrat");
        if (typeContrat != null && !typeContrat.isEmpty()) {
            professionnel.setTypeContrat(TypeContrat.valueOf(typeContrat));
        }
        
        return professionnel;
    }
}
