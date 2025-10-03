package repository;

import config.db.DatabaseConnection;
import entities.enums.Decision;
import entities.models.Credit;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CreditRepository {

    public Credit create(Credit credit) throws SQLException {
        String sql = "INSERT INTO credit (personne_id, date_de_credit, montant_demande, montant_octroye, taux_interet, duree_en_mois, type_credit, decision) VALUES (?, ?, ?, ?, ?, ?, ?, ?::decision_enum) RETURNING id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, credit.getPersonneId());
            stmt.setDate(2, Date.valueOf(credit.getDateCredit()));
            stmt.setDouble(3, credit.getMontantDemande());
            stmt.setDouble(4, credit.getMontantOctroye());
            stmt.setDouble(5, credit.getTauxInteret());
            stmt.setInt(6, credit.getDureeEnMois());
            stmt.setString(7, credit.getTypeCredit());
            stmt.setString(8, credit.getDecision().name());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    credit.setId(rs.getInt("id"));
                }
            }
            
            return credit;
        }
    }

    public Optional<Credit> findById(int id) throws SQLException {
        String sql = "SELECT * FROM credit WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCredit(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Credit> findByPersonneId(int personneId) throws SQLException {
        String sql = "SELECT * FROM credit WHERE personne_id = ? ORDER BY date_de_credit DESC";
        List<Credit> credits = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, personneId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    credits.add(mapResultSetToCredit(rs));
                }
            }
        }
        return credits;
    }

    public List<Credit> findAll() throws SQLException {
        String sql = "SELECT * FROM credit ORDER BY date_de_credit DESC";
        List<Credit> credits = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                credits.add(mapResultSetToCredit(rs));
            }
        }
        return credits;
    }

    public Credit update(Credit credit) throws SQLException {
        String sql = "UPDATE credit SET montant_demande = ?, montant_octroye = ?, taux_interet = ?, duree_en_mois = ?, type_credit = ?, decision = ?::decision_enum WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, credit.getMontantDemande());
            stmt.setDouble(2, credit.getMontantOctroye());
            stmt.setDouble(3, credit.getTauxInteret());
            stmt.setInt(4, credit.getDureeEnMois());
            stmt.setString(5, credit.getTypeCredit());
            stmt.setString(6, credit.getDecision().name());
            stmt.setInt(7, credit.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating credit failed, no rows affected.");
            }
        }
        return credit;
    }

    public boolean deleteById(int id) throws SQLException {
        String sql = "DELETE FROM credit WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Check if a person has existing credits (to determine if they are new or existing client)
    public boolean hasExistingCredits(int personneId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM credit WHERE personne_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, personneId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private Credit mapResultSetToCredit(ResultSet rs) throws SQLException {
        Credit credit = new Credit();
        credit.setId(rs.getInt("id"));
        credit.setPersonneId(rs.getInt("personne_id"));
        
        Date dateCredit = rs.getDate("date_de_credit");
        if (dateCredit != null) {
            credit.setDateCredit(dateCredit.toLocalDate());
        }
        
        credit.setMontantDemande(rs.getDouble("montant_demande"));
        credit.setMontantOctroye(rs.getDouble("montant_octroye"));
        credit.setTauxInteret(rs.getDouble("taux_interet"));
        credit.setDureeEnMois(rs.getInt("duree_en_mois"));
        credit.setTypeCredit(rs.getString("type_credit"));
        
        String decision = rs.getString("decision");
        if (decision != null && !decision.isEmpty()) {
            try {
                credit.setDecision(Decision.valueOf(decision));
            } catch (IllegalArgumentException e) {
                System.err.println("Warning: Unknown decision enum value '" + e.getMessage() + "' in database. Setting decision to null." );
            }
        }
        
        return credit;
    }
}