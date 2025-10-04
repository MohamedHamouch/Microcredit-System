package main.repository.impl;

import main.config.DatabaseConfig;
import main.enums.EnumRole;
import main.enums.EnumSitFam;
import main.model.Professionnel;
import main.model.Person;
import main.repository.interfaces.PersonRepository;
import main.repository.interfaces.ProfessionnelRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class ProfessionnelRepositoryImpl implements ProfessionnelRepository {
    private final Connection conn = DatabaseConfig.getInstance().getConnection();
    private final PersonRepository personRepository = new PersonRepositoryImpl();

    @Override
    public Optional<Professionnel> insertProfessionnel(Professionnel professionnel) {
        String insertQuery = "INSERT INTO professionnel (id, revenu, immatriculationFiscale, secteurActivite, Activite) VALUES (?, ?, ?, ?, ?)";
        try {
            Person person = personRepository.insertPerson(professionnel).orElseThrow(() -> new RuntimeException("Erreur lors de l'insertion de person"));
            PreparedStatement pstmt = conn.prepareStatement(insertQuery);
            pstmt.setInt(1, person.getId());
            pstmt.setDouble(2, professionnel.getRevenu());
            pstmt.setDouble(3, professionnel.getImmatriculationFiscale());
            pstmt.setString(4, professionnel.getSecteurActivite());
            pstmt.setString(5, professionnel.getActivite());

            int rowsAff = pstmt.executeUpdate();
            if (rowsAff > 0) {
                Professionnel newProfessionnel = this.findProfessionnel(person.getId()).orElseThrow(() -> new RuntimeException("Aucun Client Professionnwl trouvé avec l'id: " + person.getId()));

                return Optional.of(newProfessionnel);
            }
            return Optional.empty();
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException("Erreur SQL lors d'insertion du professionnel:" + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Professionnel> findProfessionnel(Integer id) {
        String findQurey = "SELECT * FROM professionnel WHERE id = ?";
        try {
            Person person = personRepository.findPerson(id).orElseThrow(() -> new RuntimeException("Aucun Client Professionnwl trouvé avec l'id: " + id));
            Professionnel professionnel = new Professionnel(person.getId(), person.getNom(), person.getPrenom(), person.getEmail(), person.getDateNaissance(), person.getVille(), person.getNombreEnfants(), person.getInvestissement(), person.getPlacement(), person.getSituationFamiliale(), person.getCreatedAt(), person.getScore(), person.getRole());
            PreparedStatement pstmt = conn.prepareStatement(findQurey);
            pstmt.setInt(1, id);

            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                professionnel.setRevenu(resultSet.getDouble("revenu"));
                professionnel.setImmatriculationFiscale(resultSet.getDouble("immatriculationFiscale"));
                professionnel.setSecteurActivite(resultSet.getString("secteurActivite"));
                professionnel.setActivite(resultSet.getString("Activite"));

                return Optional.of(professionnel);
            }
            return Optional.empty();
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<Professionnel> updateProfessionnel(Professionnel professionnel, Map<String, Object> updates) {
        Map<String, List<String>> attributesAccount = new HashMap<>();
        Map<String, Object> listUpdatesPerson = new HashMap<>();
        attributesAccount.put("person", new ArrayList<String>(Arrays.asList("nom", "prenom", "email", "dateNaissance", "ville", "nombreEnfants", "investissement", "placement", "situationFamiliale", "createdAt", "score", "role")));
        attributesAccount.put("professionnel", new ArrayList<String>(Arrays.asList("revenu", "immatriculationFiscale", "secteurActivite", "Activite")));

        StringBuilder updatePerfoQuery = new StringBuilder("UPDATE professionnel SET ");

        boolean updatePerson = false;
        int i = 0;
        for (String key : updates.keySet()) {
            if (attributesAccount.get("person").contains(key)) {
                listUpdatesPerson.put(key, updates.get(key));
                updatePerson = true;
            }
            if (attributesAccount.get("professionnel").contains(key)) updatePerfoQuery.append(key).append(" = ?, ");
            i++;
        }
        String updatePerfoQueryStr = updatePerfoQuery.substring(0, updatePerfoQuery.length() -2);
        updatePerfoQueryStr += " WHERE id = ?";

        try {
            if (updatePerson) personRepository.updatePerson(professionnel, listUpdatesPerson).orElseThrow(() -> new RuntimeException("Aucun employe trouvé avec l'id"));
            PreparedStatement pstmt = conn.prepareStatement(updatePerfoQueryStr);
            i = 1;
            for (String key : updates.keySet()) {
                if (attributesAccount.get("professionnel").contains(key)) pstmt.setObject(i++, updates.get(key));
            }
            pstmt.setInt(i, professionnel.getId());
            int rowsAff = pstmt.executeUpdate();
            if (rowsAff > 0 ) {
                return this.findProfessionnel(professionnel.getId());
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public List<Professionnel> selectProfessionnels() {
        String selectQurey = "SELECT * FROM person p JOIN professionnel pf ON p.id = pf.id";
        try (PreparedStatement pstmt = conn.prepareStatement(selectQurey)){
            ResultSet resultSet = pstmt.executeQuery();
            List<Professionnel> professionnels = new ArrayList<>();
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String email = resultSet.getString("email");
                LocalDate dateNaissance = resultSet.getDate("dateNaissance").toLocalDate();
                String ville = resultSet.getString("ville");
                Integer nombreEnfants = resultSet.getInt("nombreEnfants");
                Boolean investissement = resultSet.getBoolean("investissement");
                Boolean placement = resultSet.getBoolean("placement");
                EnumSitFam situationFamiliale = EnumSitFam.valueOf(resultSet.getString("situationFamiliale"));
                LocalDateTime createdAt = resultSet.getTimestamp("createdAt").toLocalDateTime();
                Integer score = resultSet.getInt("score");
                Double revenu = resultSet.getDouble("revenu");
                Double immatriculationFiscale = resultSet.getDouble("immatriculationFiscale");
                String secteurActivite = resultSet.getString("secteurActivite");
                String Activite = resultSet.getString("Activite");
                EnumRole role = EnumRole.valueOf(resultSet.getString("role"));

                professionnels.add(new Professionnel(id, nom, prenom, email, dateNaissance, ville, nombreEnfants,
                        investissement, placement, situationFamiliale, createdAt, score, role,
                        revenu, immatriculationFiscale, secteurActivite, Activite));
            }
            return professionnels;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
