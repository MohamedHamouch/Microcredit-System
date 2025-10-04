package main.controller;

import main.model.Credit;
import main.service.interfaces.CreditService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class CreditController {
    private final CreditService creditService;

    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }

    public Map<String, Object> create(Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();
        try {
            for (String key : data.keySet()) if (data.get(key) == null) throw new RuntimeException("La valeur de " + key + " ne peut pas être null");
            Credit credit = this.creditService.ajouterCredit(this.instancedEmp(data));
            result.put("credit", credit);
            result.put("message", "✅ Le Credit est ajouter avec success");
            result.put("erreur", "");

            return result;
        } catch (RuntimeException e) {
            result.put("erreur", "❌ Erreur: " + e.getMessage());
            result.put("credit", "");
            result.put("message", "");
            return result;
        }
    }

    private Credit instancedEmp(Map<String , Object> data) {
        return new Credit(
                (LocalDateTime) data.get("dateCredit"), (Double) data.get("montantDemande"),
                (Double) data.get("montantOctroye"), (String) data.get("typeCredit"), (Integer) data.get("person_id")
        );
    }
}
