package repository;

import config.db.DatabaseConnection;
import entities.models.Echeance;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EcheanceRepository {

    public Echeance create(Echeance e, Integer creditId) throws SQLException {
        String sql = "INSERT INTO echeance (credit_id, date_echeance, date_de_paiement, mensualite, statut_paiement) VALUES (?,?,?,?,?::statut_paiement_enum) RETURNING id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, creditId);
            ps.setDate(2, Date.valueOf(e.getDateEcheance()));
            
            if (e.getDateDePaiment() != null) {
                ps.setDate(3, Date.valueOf(e.getDateDePaiment()));
            } else {
                ps.setNull(3, java.sql.Types.DATE);
            }
            
            ps.setDouble(4, e.getMensualite());
            if (e.getStatutPaiement() != null) {
                ps.setString(5, e.getStatutPaiement().name());
            } else {
                ps.setNull(5, java.sql.Types.VARCHAR);
            }
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) e.setId(rs.getInt("id")); }
            return e;
        }
    }

    public Echeance findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM echeance WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Echeance e = new Echeance();
                    e.setId(rs.getInt("id"));
                    Date d = rs.getDate("date_echeance"); if (d!=null) e.setDateEcheance(d.toLocalDate());
                    Date dp = rs.getDate("date_de_paiement"); if (dp!=null) e.setDateDePaiment(dp.toLocalDate());
                    e.setMensualite(rs.getDouble("mensualite"));
                    String sp = rs.getString("statut_paiement"); if (sp!=null) try { e.setStatutPaiement(entities.enums.StatutPaiement.valueOf(sp)); } catch (Exception ignore) {}
                    return e;
                }
            }
        }
        return null;
    }

    public List<Echeance> findByCreditId(Integer creditId) throws SQLException {
        List<Echeance> echeances = new ArrayList<>();
        String sql = "SELECT * FROM echeance WHERE credit_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, creditId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Echeance e = new Echeance();
                    e.setId(rs.getInt("id"));
                    e.setCreditId(rs.getInt("credit_id"));
                    Date d = rs.getDate("date_echeance"); if (d!=null) e.setDateEcheance(d.toLocalDate());
                    Date dp = rs.getDate("date_de_paiement"); if (dp!=null) e.setDateDePaiment(dp.toLocalDate());
                    e.setMensualite(rs.getDouble("mensualite"));
                    String sp = rs.getString("statut_paiement"); if (sp!=null) try { e.setStatutPaiement(entities.enums.StatutPaiement.valueOf(sp)); } catch (Exception ignore) {}
                    echeances.add(e);
                }
            }
        }
        return echeances;
    }

    public List<Echeance> findAll() throws SQLException {
        List<Echeance> out = new ArrayList<>();
        String sql = "SELECT * FROM echeance";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Echeance e = new Echeance();
                e.setId(rs.getInt("id"));
                Date d = rs.getDate("date_echeance"); if (d!=null) e.setDateEcheance(d.toLocalDate());
                Date dp = rs.getDate("date_de_paiement"); if (dp!=null) e.setDateDePaiment(dp.toLocalDate());
                e.setMensualite(rs.getDouble("mensualite"));
                String sp = rs.getString("statut_paiement"); if (sp!=null) try { e.setStatutPaiement(entities.enums.StatutPaiement.valueOf(sp)); } catch (Exception ignore) {}
                out.add(e);
            }
        }
        return out;
    }

    public Echeance update(Echeance e) throws SQLException {
        String sql = "UPDATE echeance SET date_echeance=?, date_de_paiement=?, mensualite=?, statut_paiement=?::statut_paiement_enum WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(e.getDateEcheance()));
            ps.setDate(2, Date.valueOf(e.getDateDePaiment()));
            ps.setDouble(3, e.getMensualite());
            ps.setString(4, e.getStatutPaiement().name());
            ps.setInt(5, e.getId());
            int u = ps.executeUpdate(); return u>0?e:null;
        }
    }

    public boolean delete(Integer id) throws SQLException {
        String sql = "DELETE FROM echeance WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int r = ps.executeUpdate(); return r>0;
        }
    }
}
