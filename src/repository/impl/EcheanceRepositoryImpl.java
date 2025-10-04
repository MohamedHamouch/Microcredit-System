package main.repository.impl;

import main.config.DatabaseConfig;
import main.enums.StatutPaiement;
import main.model.Echeance;
import main.repository.interfaces.EcheanceRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class EcheanceRepositoryImpl implements EcheanceRepository {
    private final Connection conn = DatabaseConfig.getInstance().getConnection();

    @Override
    public Optional<Echeance> insertEcheance(Echeance echeance) {
        String insertQuery = "INSERT INTO echeance (dateEcheance, mensualite, statutPaiement, credit_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(echeance.getDateEcheance()));
            pstmt.setDouble(2, echeance.getMensualite());
            pstmt.setString(3, echeance.getStatutPaiement().toString());
            pstmt.setInt(4, echeance.getCredit_id());

            int rowsAff = pstmt.executeUpdate();
            if (rowsAff > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        echeance.setId(generatedKeys.getInt(1));
                        return Optional.of(echeance);
                    }
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<Echeance> findEcheance(Integer id) {
        String findQurey = "SELECT * FROM echeance WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(findQurey)) {
            pstmt.setInt(1, id);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                Integer echeanceId = resultSet.getInt("id");
                LocalDateTime dateEcheance = resultSet.getTimestamp("dateEcheance").toLocalDateTime();;
                Double mensualite = resultSet.getDouble("mensualite");;
                Timestamp tsDatePaiement = resultSet.getTimestamp("datePaiement");
                LocalDateTime datePaiement = tsDatePaiement != null ? tsDatePaiement.toLocalDateTime() : null;
                StatutPaiement statutPaiement = StatutPaiement.valueOf(resultSet.getString("statutPaiement"));;
                Integer credit_id = resultSet.getInt("credit_id");

                return Optional.of(new Echeance(echeanceId, dateEcheance, mensualite, datePaiement, statutPaiement, credit_id));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<Echeance> updateEcheance(Echeance echeance, Map<String, Object> updates) {
        StringBuilder updateQuery = new StringBuilder("UPDATE echeance SET ");
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
            pstmt.setInt(i, echeance.getId());

            int rowsAff = pstmt.executeUpdate();
            if (rowsAff > 0) {
                return this.findEcheance(echeance.getId());
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public List<Echeance> selectEcheances() {
        String findQurey = "SELECT * FROM echeance";
        try (PreparedStatement pstmt = conn.prepareStatement(findQurey)) {
            ResultSet resultSet = pstmt.executeQuery();
            List<Echeance> echeances = new ArrayList<>();
            while (resultSet.next()) {
                Integer echeanceId = resultSet.getInt("id");
                LocalDateTime dateEcheance = resultSet.getTimestamp("dateEcheance").toLocalDateTime();
                Double mensualite = resultSet.getDouble("mensualite");
                Timestamp tsDatePaiement = resultSet.getTimestamp("datePaiement");
                LocalDateTime datePaiement = tsDatePaiement != null ? tsDatePaiement.toLocalDateTime() : null;
                StatutPaiement statutPaiement = StatutPaiement.valueOf(resultSet.getString("statutPaiement"));
                Integer credit_id = resultSet.getInt("credit_id");

                echeances.add(new Echeance(echeanceId, dateEcheance, mensualite, datePaiement, statutPaiement, credit_id));
            }
            return echeances;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Boolean deleteEcheance(Echeance echeance) {
        String deleteQuery = "DELETE FROM echeance WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteQuery)){
            pstmt.setInt(1, echeance.getId());
            int rowsAff = pstmt.executeUpdate();
            return rowsAff > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public List<Echeance> selectCreditEcheances(Integer id) {
        String findQurey = "SELECT * FROM echeance WHERE credit_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(findQurey)) {
            pstmt.setInt(1, id);
            ResultSet resultSet = pstmt.executeQuery();
            List<Echeance> echeances = new ArrayList<>();
            while (resultSet.next()) {
                Integer echeanceId = resultSet.getInt("id");
                LocalDateTime dateEcheance = resultSet.getTimestamp("dateEcheance").toLocalDateTime();;
                Double mensualite = resultSet.getDouble("mensualite");
                Timestamp tsDatePaiement = resultSet.getTimestamp("datePaiement");
                LocalDateTime datePaiement = tsDatePaiement != null ? tsDatePaiement.toLocalDateTime() : null;
                StatutPaiement statutPaiement = StatutPaiement.valueOf(resultSet.getString("statutPaiement"));;
                Integer credit_id = resultSet.getInt("credit_id");;

                echeances.add(new Echeance(echeanceId, dateEcheance, mensualite, datePaiement, statutPaiement, credit_id));
            }
            return echeances;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
