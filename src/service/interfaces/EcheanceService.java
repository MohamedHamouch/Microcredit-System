package main.service.interfaces;

import main.model.Echeance;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EcheanceService {
    Echeance ajouterEcheance(Echeance echeance);
    Echeance findEcheance(Integer id);
    Echeance updateEcheance(Integer echean_id, Map<String, Object> updates);
    List<Echeance> getAllEcheances();
    Boolean deleteEcheance(Integer echeance_id);
    List<Echeance> selectPersonEcheances(Integer person_id);
}
