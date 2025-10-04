package main.controller;

import main.enums.EnumRole;
import main.enums.EnumSecteur;
import main.enums.EnumSitFam;
import main.model.Employe;
import main.service.interfaces.EmployeService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class EmployeController {
    private final EmployeService employeService;

    public EmployeController(EmployeService employeService ) {
        this.employeService = employeService;
    }

    public Map<String, Object> create(Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();
        try {
            for (String key : data.keySet()) if (data.get(key) == null) throw new RuntimeException("La valeur de " + key + " ne peut pas être null");
            Employe employe = this.employeService.ajouterEmploye(this.instancedEmp(data));
            result.put("employe", employe);
            result.put("message", "✅ Le client est ajouter avec success");
            result.put("erreur", "");

            return result;
        } catch (RuntimeException e) {
            result.put("erreur", "❌ Erreur: " + e.getMessage());
            result.put("employe", "");
            result.put("message", "");
            return result;
        }
    }

    public String delete(Integer person_id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Boolean delete = this.employeService.deleteEmploye(person_id);
            if (delete) {
                return "🗑️ Employe avec ID " + person_id + " supprimé avec succès.";
            } else {
                return "⚠️ Aucun Employe trouvé avec ID " + person_id;
            }
        } catch (RuntimeException e) {
            return "❌ Erreur: " + e.getMessage();
        }
    }

    private Employe instancedEmp(Map<String , Object> data) {
        return new Employe(
                (String) data.get("nom"), (String) data.get("prenom"),
                (String) data.get("email"), (LocalDate) data.get("dateNaissance"),
                (String) data.get("ville"), (Integer) data.get("nombreEnfants"),
                (Boolean) data.get("investissement"), (Boolean) data.get("placement"),
                (EnumSitFam) data.get("situationFamiliale"), (LocalDateTime) data.get("createdAt"),
                (Integer) data.get("score"), (EnumRole) data.get("role"),
                (Double) data.get("salaire"), (Integer) data.get("anciennete"),
                (String) data.get("poste"), (String) data.get("typeContrat"),
                (EnumSecteur) data.get("secteur")
        );
    }
}
