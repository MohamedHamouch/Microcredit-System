package main.service.impl;

import main.model.Employe;
import main.repository.interfaces.EmployeRepository;
import main.repository.interfaces.PersonRepository;
import main.service.interfaces.EmployeService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmployeServiceImpl implements EmployeService {
    private final EmployeRepository employeRepository;
    private final PersonRepository personRepository;

    public EmployeServiceImpl(EmployeRepository employeRepository, PersonRepository personRepository) {
        this.employeRepository = employeRepository;
        this.personRepository = personRepository;
    }


    @Override
    public Employe ajouterEmploye(Employe employe) {
        if (employe == null) throw new IllegalArgumentException("L'employé ne peut pas être null");
        try {
            return employeRepository.insertEmploye(employe)
                    .orElseThrow(() -> new RuntimeException("Impossible d'ajouter l'employe: " + employe.getNom() + employe.getPrenom()));
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Employe findEmploye(Integer id) {
        if (id == null) throw new  IllegalArgumentException("L'id employe ne peut pas être null");
        try {
            return employeRepository.findEmploye(id)
                    .orElseThrow(() -> new RuntimeException("Aucun employe trouvé avec l'id: " + id));
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Employe updateEmploye(Integer id, Map<String, Object> update) {
        if (id == null) throw new  IllegalArgumentException("L'id employe ne peut pas être null");
        if (update.isEmpty()) throw new  RuntimeException("Les modifications ne peut pas être vide");
        try {
            Employe employe = this.findEmploye(id);
            return employeRepository.updateEmploye(employe, update)
                    .orElseThrow(() -> new RuntimeException("Impossible de modifier l'employe d'id: " + id));
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public List<Employe> getAllEmployes() {
        try {
            return employeRepository.selectEmployes().stream()
                    .sorted((e1, e2) -> {
                        int compare = e1.getNom().compareTo(e2. getNom());
                        if (compare == 0 ) return e1.getPrenom().compareTo(e2.getPrenom());
                        return e1.getNom().compareTo(e2.getNom());
                    })
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Boolean deleteEmploye(Integer id) {
        if (id == null) throw new  IllegalArgumentException("L'id d'employe ne peut pas être null");
        try {
            Employe employe = this.findEmploye(id);
            return personRepository.deletePerson(employe);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
