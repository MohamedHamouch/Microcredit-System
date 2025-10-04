package main.controller;

import main.enums.EnumRole;
import main.enums.EnumSecteur;
import main.enums.EnumSitFam;
import main.model.Employe;
import main.model.Professionnel;
import main.service.interfaces.ProfessionnelService;

import java.security.PermissionCollection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ProfessionnelController {
    private final ProfessionnelService professionnelService;

    public ProfessionnelController(ProfessionnelService professionnelService) {
        this.professionnelService = professionnelService;
    }

    public Map<String, Object> create(Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();
        try {
            for (String key : data.keySet()) if (data.get(key) == null) throw new RuntimeException("La valeur de " + key + " ne peut pas être null");
            Professionnel professionnel = this.professionnelService.ajouterProfessionnel(this.instancedProff(data));
            result.put("professionnel", professionnel);
            result.put("message", "✅ Le client est ajouter avec success");
            result.put("erreur", "");

            return result;
        } catch (RuntimeException e) {
            result.put("erreur", "❌ Erreur: " + e.getMessage());
            result.put("professionnel", "");
            result.put("message", "");
            return result;
        }
    }

    private Professionnel instancedProff(Map<String , Object> data) {
        return new Professionnel(
                (String) data.get("nom"), (String) data.get("prenom"), (String) data.get("email"),
                (LocalDate) data.get("dateNaissance"), (String) data.get("ville"),
                (Integer) data.get("nombreEnfants"), (Boolean) data.get("investissement"),
                (Boolean) data.get("placement"), (EnumSitFam) data.get("situationFamiliale"),
                (LocalDateTime) data.get("createdAt"), (Integer) data.get("score"),
                (EnumRole) data.get("role"),(Double) data.get("revenu"), (Double) data.get("immatriculationFiscale"),
                (String) data.get("secteurActivite"), (String) data.get("Activite")
        );
    }

    public String delete(Integer person_id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Boolean delete = this.professionnelService.deleteProfessionnel(person_id);
            if (delete) {
                return "🗑️ Client professionnel avec ID " + person_id + " supprimé avec succès.";
            } else {
                return "⚠️ Aucun Client professionnel trouvé avec ID " + person_id;
            }
        } catch (RuntimeException e) {
            return "❌ Erreur: " + e.getMessage();
        }
    }
}
