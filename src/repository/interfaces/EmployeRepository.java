package main.repository.interfaces;

import main.model.Employe;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EmployeRepository {
    Optional<Employe> insertEmploye(Employe employe);
    Optional<Employe> findEmploye(Integer id);
    Optional<Employe> updateEmploye(Employe empl, Map<String, Object> update);
    List<Employe> selectEmployes();
}
