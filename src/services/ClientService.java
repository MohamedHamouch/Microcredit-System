package services;

import entities.models.Employe;
import entities.models.Personne;
import entities.models.Professionnel;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import repository.EmployeRepository;
import repository.PersonneRepository;
import repository.ProfessionnelRepository;

public class ClientService {

    private PersonneRepository personneRepository;
    private EmployeRepository employeRepository;
    private ProfessionnelRepository professionnelRepository;

    public ClientService() {
        this.personneRepository = new PersonneRepository();
        this.employeRepository = new EmployeRepository();
        this.professionnelRepository = new ProfessionnelRepository();
    }

    public Employe createEmployeClient(Employe employe) throws SQLException {
        int score = ScoringService.calculateScore(employe);
        employe.setScore(score);

        return employeRepository.create(employe);
    }

    public Professionnel createProfessionnelClient(Professionnel professionnel) throws SQLException {
        int score = ScoringService.calculateScore(professionnel);
        professionnel.setScore(score);

        return professionnelRepository.create(professionnel);
    }

    public Employe updateEmployeClient(Employe employe) throws SQLException {
        int score = ScoringService.calculateScore(employe);
        employe.setScore(score);

        return employeRepository.update(employe);
    }

    public Professionnel updateProfessionnelClient(Professionnel professionnel) throws SQLException {
        int score = ScoringService.calculateScore(professionnel);
        professionnel.setScore(score);

        return professionnelRepository.update(professionnel);
    }

    public Optional<Personne> findPersonneById(int id) throws SQLException {
        return personneRepository.findById(id);
    }

    public Optional<Employe> findEmployeByPersonneId(int personneId) throws SQLException {
        return employeRepository.findByPersonneId(personneId);
    }

    public Optional<Professionnel> findProfessionnelByPersonneId(int personneId) throws SQLException {
        return professionnelRepository.findByPersonneId(personneId);
    }

    public List<Personne> findAllPersonnes() throws SQLException {
        return personneRepository.findAll();
    }

    public List<Employe> findAllEmployes() throws SQLException {
        return employeRepository.findAll();
    }

    public List<Professionnel> findAllProfessionnels() throws SQLException {
        return professionnelRepository.findAll();
    }

    public boolean deleteClient(int personneId) throws SQLException {
        Optional<Employe> employe = employeRepository.findByPersonneId(personneId);
        if (employe.isPresent()) {
            employeRepository.deleteByPersonneId(personneId);
        }

        Optional<Professionnel> professionnel = professionnelRepository.findByPersonneId(personneId);
        if (professionnel.isPresent()) {
            professionnelRepository.deleteByPersonneId(personneId);
        }

        return personneRepository.deleteById(personneId);
    }

    public static double getMonthlyIncome(Personne personne) {
        if (personne instanceof Employe) {
            Employe employe = (Employe) personne;
            return employe.getSalaire() != null ? employe.getSalaire() : 0.0;
        } else if (personne instanceof Professionnel) {
            Professionnel professionnel = (Professionnel) personne;
            return professionnel.getRevenu() != null ? professionnel.getRevenu() : 0.0;
        }
        return 0.0;
    }
}