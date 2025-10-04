package main.repository.interfaces;

import main.model.Credit;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CreditRepository {
    Optional<Credit> insertCredit(Credit credit);
    Optional<Credit> findCredit(Integer id);
    Optional<Credit> updateCredit(Credit cred, Map<String, Object> update);
    List<Credit> selectCredits();
    Boolean deleteCredit(Credit credit);
    List<Credit> selectPersonCredits(Integer id);
}
