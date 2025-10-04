package main.service.interfaces;

import main.model.Credit;
import main.model.Echeance;

import java.util.List;
import java.util.Map;

public interface CreditService {
    Credit ajouterCredit(Credit credit);
    Credit findCredit(Integer id);
    Credit updateCredit(Integer id, Map<String, Object> update);
    List<Credit> getAllCredits();
    Boolean deleteCredit(Integer id);
    List<Credit> getPersonCredits(Integer person_id);
    List<Echeance> selectCreditEcheances(Integer id);
}
