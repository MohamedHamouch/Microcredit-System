package main.repository.impl;

import main.config.DatabaseConfig;
import main.enums.EnumRole;
import main.enums.EnumSecteur;
import main.enums.EnumSitFam;
import main.model.Employe;
import main.model.Person;
import main.repository.interfaces.EmployeRepository;
import main.repository.interfaces.PersonRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class EmployeRepositoryImp  implements EmployeRepository {
    private final Connection conn = DatabaseConfig.getInstance().getConnection();
    private final PersonRepository personRepository = new PersonRepositoryImpl();

    @Override
    public Optional<Employe> insertEmploye(Employe employe) {
        String insertQuery = "INSERT INTO employe (id, salaire, anciennete, poste, typeContrat, secteur) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            Person person = personRepository.insertPerson(employe).orElseThrow(() -> new RuntimeException("Impossible d'ajouter ce person"));
            PreparedStatement pstmt = conn.prepareStatement(insertQuery);
            pstmt.setInt(1, person.getId());
            pstmt.setDouble(2, employe.getSalaire());
            pstmt.setInt(3, employe.getAnciennete());
            pstmt.setString(4, employe.getPoste());
            pstmt.setString(5, employe.getTypeContrat());
            pstmt.setString(6, employe.getSecteur().toString());

            int rowsAff = pstmt.executeUpdate();
            if (rowsAff > 0) {
                Employe newEmploye = this.findEmploye(person.getId()).orElseThrow(() -> new RuntimeException("Aucun employe trouvé avec l'id: " + person.getId()));

                return Optional.of(newEmploye);
            }
            return Optional.empty();
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException("Erreur SQL lors d'insertion du employe:" + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Employe> findEmploye(Integer id) {
        String findQurey = "SELECT * FROM employe WHERE id = ?";
        try {
            Person person = personRepository.findPerson(id).orElseThrow(() -> new RuntimeException("Aucun person trouvé avec l'id: " + id));
            Employe employe = new Employe(person.getId(), person.getNom(), person.getPrenom(), person.getEmail(), person.getDateNaissance(), person.getVille(), person.getNombreEnfants(), person.getInvestissement(), person.getPlacement(), person.getSituationFamiliale(), person.getCreatedAt(), person.getScore(), person.getRole());
            PreparedStatement pstmt = conn.prepareStatement(findQurey);
            pstmt.setInt(1, id);

            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                employe.setSalaire(resultSet.getDouble("salaire"));
                employe.setAnciennete(resultSet.getInt("anciennete"));
                employe.setPoste(resultSet.getString("poste"));
                employe.setTypeContrat(resultSet.getString("typeContrat"));
                employe.setSecteur(EnumSecteur.valueOf(resultSet.getString("secteur")));

                return Optional.of(employe);
            }
            return Optional.empty();
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<Employe> updateEmploye(Employe employe, Map<String, Object> updates) {
        Map<String, List<String>> attributesAccount = new HashMap<>();
        Map<String, Object> updatesPerson = new HashMap<>();
        attributesAccount.put("person", new ArrayList<String>(Arrays.asList("nom", "prenom", "email", "dateNaissance", "ville", "nombreEnfants", "investissement", "placement", "situationFamiliale", "createdAt", "score", "role")));
        attributesAccount.put("employe", new ArrayList<String>(Arrays.asList("salaire", "anciennete", "poste", "typeContrat", "secteur")));

        StringBuilder updateEmpQuery = new StringBuilder("UPDATE employe SET ");

        boolean updatePerson = false;
        int i = 0;
        for (String key : updates.keySet()) {
            if (attributesAccount.get("person").contains(key)) {
                updatesPerson.put(key, updates.get(key));
                updatePerson = true;
            }
            if (attributesAccount.get("employe").contains(key)) updateEmpQuery.append(key).append(" = ?, ");
            i++;
        }
        String updateEmpQueryStr = updateEmpQuery.substring(0, updateEmpQuery.length() -2);
        updateEmpQueryStr += " WHERE id = ?";

        try {
            if (updatePerson) personRepository.updatePerson(employe, updatesPerson).orElseThrow(() -> new RuntimeException("Impossible de modifier ce person d'id: " + employe.getId()));
            PreparedStatement pstmt = conn.prepareStatement(updateEmpQueryStr);
            i = 1;
            for (String key : updates.keySet()) {
                if (attributesAccount.get("employe").contains(key)) pstmt.setObject(i++, updates.get(key));
            }
            pstmt.setInt(i, employe.getId());
            int rowsAff = pstmt.executeUpdate();
            if (rowsAff > 0 ) {
                return this.findEmploye(employe.getId());
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public List<Employe> selectEmployes() {
        String selectQurey = "SELECT * FROM person p JOIN employe e ON p.id = e.id";
        try (PreparedStatement pstmt = conn.prepareStatement(selectQurey)){
            ResultSet resultSet = pstmt.executeQuery();
            List<Employe> employes = new ArrayList<>();
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
                Double salaire = resultSet.getDouble("salaire");
                Integer anciennete = resultSet.getInt("anciennete");
                String poste = resultSet.getString("poste");
                String typeContrat = resultSet.getString("typeContrat");
                EnumSecteur secteur = EnumSecteur.valueOf(resultSet.getString("secteur"));
                EnumRole role = EnumRole.valueOf(resultSet.getString("role"));

                employes.add(new Employe(id, nom, prenom, email, dateNaissance, ville, nombreEnfants,
                        investissement, placement, situationFamiliale, createdAt, score, role,
                        salaire, anciennete, poste, typeContrat, secteur));
            }
            return employes;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
