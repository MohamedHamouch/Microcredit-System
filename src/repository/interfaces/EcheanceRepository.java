package main.repository.interfaces;

import main.model.Echeance;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EcheanceRepository {
    Optional<Echeance> insertEcheance(Echeance echeance);
    Optional<Echeance> findEcheance(Integer id);
    Optional<Echeance> updateEcheance(Echeance echean, Map<String, Object> updates);
    List<Echeance> selectEcheances();
    Boolean deleteEcheance(Echeance echeance);
    List<Echeance> selectCreditEcheances(Integer id);
}
