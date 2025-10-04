package main.service.interfaces;

import main.model.Employe;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EmployeService {
    Employe ajouterEmploye(Employe employe);
    Employe findEmploye(Integer id);
    Employe updateEmploye(Integer id, Map<String, Object> update);
    List<Employe> getAllEmployes();
    Boolean deleteEmploye(Integer id);
}
