package repository;

import config.db.DatabaseConnection;
import entities.enums.TypeIncident;
import entities.models.Incident;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IncidentRepository {

    public Incident create(Incident i) throws SQLException {
        String sql = "INSERT INTO incident (date_incident, echeance_id, score, type_incident) VALUES (?,?,?,?::typeincident_enum) RETURNING id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(i.getDateIncident()));
            ps.setInt(2, i.getEcheanceId());
            ps.setInt(3, i.getScore());
            ps.setString(4, i.getTypeIncident().name());
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) i.setId(rs.getInt("id")); }
            return i;
        }
    }

    public Incident findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM incident WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Incident i = new Incident();
                    i.setId(rs.getInt("id"));
                    i.setDateIncident(rs.getDate("date_incident").toLocalDate());
                    i.setEcheanceId(rs.getInt("echeance_id"));
                    i.setScore(rs.getInt("score"));
                    String ti = rs.getString("type_incident"); if (ti!=null) try { i.setTypeIncident(TypeIncident.valueOf(ti)); } catch (Exception ignore) {}
                    return i;
                }
            }
        }
        return null;
    }

    public List<Incident> findAll() throws SQLException {
        List<Incident> out = new ArrayList<>();
        String sql = "SELECT * FROM incident";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Incident i = new Incident();
                i.setId(rs.getInt("id"));
                i.setDateIncident(rs.getDate("date_incident").toLocalDate());
                i.setEcheanceId(rs.getInt("echeance_id"));
                i.setScore(rs.getInt("score"));
                String ti = rs.getString("type_incident"); if (ti!=null) try { i.setTypeIncident(TypeIncident.valueOf(ti)); } catch (Exception ignore) {}
                out.add(i);
            }
        }
        return out;
    }

    public Incident update(Incident i) throws SQLException {
        String sql = "UPDATE incident SET date_incident=?, echeance_id=?, score=?, type_incident=?::typeincident_enum WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(i.getDateIncident()));
            ps.setInt(2, i.getEcheanceId());
            ps.setInt(3, i.getScore());
            ps.setString(4, i.getTypeIncident().name());
            ps.setInt(5, i.getId());
            int u = ps.executeUpdate(); return u>0?i:null;
        }
    }

    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM incident WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int r = ps.executeUpdate(); return r>0;
        }
    }
}
