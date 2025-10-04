package main.repository.impl;

import main.config.DatabaseConfig;
import main.enums.EnumDecision;
import main.model.Credit;
import main.repository.interfaces.CreditRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class CreditRepositoryImpl implements CreditRepository {
    private final Connection conn = DatabaseConfig.getInstance().getConnection();

    @Override
    public Optional<Credit> insertCredit(Credit credit) {
        String insertQuery = "INSERT INTO credit (dateCredit, montantDemande, montantOctroye, tauxInteret, dureeenMois, typeCredit, decision, person_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(credit.getDateCredit()));
            pstmt.setDouble(2, credit.getMontantDemande());
            pstmt.setDouble(3, credit.getMontantOctroye());
            pstmt.setDouble(4, credit.getTauxInteret());
            pstmt.setInt(5, credit.getDureeenMois());
            pstmt.setString(6, credit.getTypeCredit());
            pstmt.setString(7, credit.getDecision().toString());
            pstmt.setInt(8, credit.getPerson_id());

            int rowsAff = pstmt.executeUpdate();
            if (rowsAff > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        credit.setId(generatedKeys.getInt(1));
                        return Optional.of(credit);
                    }
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<Credit> findCredit(Integer id) {
        String findQuery = "SELECT * FROM credit WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(findQuery)) {
            pstmt.setInt(1, id);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                Integer creditId = resultSet.getInt("id");
                LocalDateTime dateCredit = resultSet.getTimestamp("dateCredit").toLocalDateTime();
                Double montantDemande = resultSet.getDouble("montantDemande");
                Double montantOctroye = resultSet.getDouble("montantOctroye");
                Double tauxInteret = resultSet.getDouble("tauxInteret");
                Integer dureeenMois = resultSet.getInt("dureeenMois");
                String typeCredit = resultSet.getString("typeCredit");
                EnumDecision decision = EnumDecision.valueOf(resultSet.getString("decision"));
                Integer person_id = resultSet.getInt("person_id");

                return Optional.of(new Credit(creditId, dateCredit, montantDemande, montantOctroye, tauxInteret, dureeenMois, typeCredit, decision, person_id));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<Credit> updateCredit(Credit credit, Map<String, Object> updates) {
        StringBuilder updateQuery = new StringBuilder("UPDATE credit SET ");
        int i = 0;
        for (String key : updates.keySet()) {
            updateQuery.append(key).append(" = ?, ");
        }
        updateQuery = new StringBuilder(updateQuery.substring(0, updateQuery.length() - 2));
        updateQuery.append(" WHERE id = ?");

        try (PreparedStatement pstmt = conn.prepareStatement(updateQuery.toString())){
            i = 1;
            for (Object value : updates.values()) {
                pstmt.setObject(i++, value);
            }
            pstmt.setInt(i, credit.getId());

            int rowsAff = pstmt.executeUpdate();
            if (rowsAff > 0) {
                return this.findCredit(credit.getId());
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public List<Credit> selectCredits() {
        String selectQuery = "SELECT * FROM credit";
        try (PreparedStatement stmt = conn.prepareStatement(selectQuery)){
            ResultSet resultSet = stmt.executeQuery();
            List<Credit> credits = new ArrayList<>();
            while (resultSet.next()) {
                Integer creditId = resultSet.getInt("id");
                LocalDateTime dateCredit = resultSet.getTimestamp("dateCredit").toLocalDateTime();
                Double montantDemande = resultSet.getDouble("montantDemande");
                Double montantOctroye = resultSet.getDouble("montantOctroye");
                Double tauxInteret = resultSet.getDouble("tauxInteret");
                Integer dureeenMois = resultSet.getInt("dureeenMois");
                String typeCredit = resultSet.getString("typeCredit");
                EnumDecision decision = EnumDecision.valueOf(resultSet.getString("decision"));
                Integer person_id = resultSet.getInt("person_id");

                credits.add(new Credit(creditId, dateCredit, montantDemande, montantOctroye, tauxInteret, dureeenMois, typeCredit, decision, person_id));
            }
            return credits;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Boolean deleteCredit(Credit credit) {
        String deleteQuery = "DELETE FROM credit WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteQuery)){
            pstmt.setInt(1, credit.getId());
            int rowsAff = pstmt.executeUpdate();
            return rowsAff > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public List<Credit> selectPersonCredits(Integer id) {
        String selectQuery = "SELECT * FROM credit WHERE person_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(selectQuery)){
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            List<Credit> credits = new ArrayList<>();
            while (resultSet.next()) {
                Integer creditId = resultSet.getInt("id");
                LocalDateTime dateCredit = resultSet.getTimestamp("dateCredit").toLocalDateTime();
                Double montantDemande = resultSet.getDouble("montantDemande");
                Double montantOctroye = resultSet.getDouble("montantOctroye");
                Double tauxInteret = resultSet.getDouble("tauxInteret");
                Integer dureeenMois = resultSet.getInt("dureeenMois");
                String typeCredit = resultSet.getString("typeCredit");
                EnumDecision decision = EnumDecision.valueOf(resultSet.getString("decision"));
                Integer person_id = resultSet.getInt("person_id");

                credits.add(new Credit(creditId, dateCredit, montantDemande, montantOctroye, tauxInteret, dureeenMois, typeCredit, decision, person_id));
            }
            return credits;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
