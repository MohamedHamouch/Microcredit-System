package main.repository.impl;

import main.config.DatabaseConfig;
import main.enums.StatutPaiement;
import main.model.Incident;
import main.repository.interfaces.IncidentRepository;
import main.utils.DatabaseException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class IncidentRepositoryImpl implements IncidentRepository {
    private final Connection conn = DatabaseConfig.getInstance().getConnection();

    @Override
    public Optional<Incident> insertIncident(Incident incident) {
        String insertQuery = "INSERT INTO incident (dateIncident, score, typeIncident, echeance_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(incident.getDateIncident()));
            pstmt.setInt(2, incident.getScore());
            pstmt.setString(3, incident.getTypeIncident().toString());
            pstmt.setInt(4, incident.getEcheance_id());

            int rowsAff = pstmt.executeUpdate();
            if (rowsAff > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        incident.setId(generatedKeys.getInt(1));
                        return Optional.of(incident);
                    }
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<Incident> findIncident(Integer id) {
        String findQuery = "SELECT * FROM incident WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(findQuery)) {
            pstmt.setInt(1, id);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                Integer incidentId = resultSet.getInt("id");
                LocalDateTime dateIncident = resultSet.getTimestamp("dateIncident").toLocalDateTime();
                Integer score = resultSet.getInt("score");
                StatutPaiement typeIncident = StatutPaiement.valueOf(resultSet.getString("typeIncident"));
                Integer echeance_id = resultSet.getInt("echeance_id");

                return Optional.of(new Incident(incidentId, dateIncident, score, typeIncident, echeance_id));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public List<Incident> selectIncidents() {
        String selectQuery = "SELECT * FROM incident";
        try (PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
            ResultSet resultSet = pstmt.executeQuery();

            List<Incident> incidents = new ArrayList<>();
            while (resultSet.next()) {
                Integer incidentId = resultSet.getInt("id");
                LocalDateTime dateIncident = resultSet.getTimestamp("dateIncident").toLocalDateTime();
                Integer score = resultSet.getInt("score");
                StatutPaiement typeIncident = StatutPaiement.valueOf(resultSet.getString("typeIncident"));
                Integer echeance_id = resultSet.getInt("echeance_id");

                incidents.add(new Incident(incidentId, dateIncident, score, typeIncident, echeance_id));
            }
            return incidents;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public List<Incident> selectEcheanceIncidents(Integer id) {
        String selectQuery = "SELECT * FROM incident WHERE echeance_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
            pstmt.setInt(1, id);
            ResultSet resultSet = pstmt.executeQuery();

            List<Incident> incidents = new ArrayList<>();
            while (resultSet.next()) {
                Integer incidentId = resultSet.getInt("id");
                LocalDateTime dateIncident = resultSet.getTimestamp("dateIncident").toLocalDateTime();
                Integer score = resultSet.getInt("score");
                StatutPaiement typeIncident = StatutPaiement.valueOf(resultSet.getString("typeIncident"));
                Integer echeance_id = resultSet.getInt("echeance_id");

                incidents.add(new Incident(incidentId, dateIncident, score, typeIncident, echeance_id));
            }
            return incidents;
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }
}
