package main.service.impl;

import main.model.Credit;
import main.model.Echeance;
import main.model.Incident;
import main.model.Person;
import main.repository.interfaces.CreditRepository;
import main.repository.interfaces.IncidentRepository;
import main.repository.interfaces.PersonRepository;
import main.service.interfaces.CreditService;
import main.service.interfaces.EcheanceService;
import main.service.interfaces.IncidentService;
import main.utils.DatabaseException;

import java.util.List;
import java.util.stream.Collectors;

public class IncidentServiceImpl implements IncidentService {
    private final IncidentRepository incidentRepository;
    private final CreditService creditService;
    private final EcheanceService echeanceService;
    private final PersonRepository personRepository;

    public IncidentServiceImpl(IncidentRepository incidentRepository, CreditService creditService, EcheanceService echeanceService, PersonRepository personRepository) {
        this.incidentRepository = incidentRepository;
        this.creditService = creditService;
        this.echeanceService = echeanceService;
        this.personRepository = personRepository;
    }

    @Override
    public Incident insertIncident(Incident incident) {
        if (incident == null) throw new IllegalArgumentException("Les information de incident ne peut pas être null");
        try {
            return incidentRepository.insertIncident(incident)
                    .orElseThrow(() -> new RuntimeException("Impossible d'ajouter d'incident"));
        } catch (RuntimeException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public Incident findIncident(Integer id) {
        if (id == null) throw new  IllegalArgumentException("L'id incident ne peut pas être null");
        try {
            return incidentRepository.findIncident(id)
                    .orElseThrow((() -> new RuntimeException("Aucun incident trouvé avec l'id: " + id)));
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public List<Incident> getAllIncidents() {
        try {
            return incidentRepository.selectIncidents().stream()
                    .sorted((incident1, incident2) -> incident1.getDateIncident().compareTo(incident2.getDateIncident()))
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public List<Incident> getEcheanceIncidents(Integer echeance_id) {
        if (echeance_id == null) throw new  IllegalArgumentException("L'id de echeance ne peut pas être null");
        try {
            Echeance echeance = echeanceService.findEcheance(echeance_id);
            return incidentRepository.selectEcheanceIncidents(echeance_id).stream()
                    .sorted((inc1, inc2) -> inc1.getDateIncident().compareTo(inc2.getDateIncident()))
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public List<Incident> getCreditIncidents(Integer credit_id) {
        if (credit_id == null) throw new  IllegalArgumentException("L'id de credit ne peut pas être null");
        try {
            Credit credit = creditService.findCredit(credit_id);
            return this.creditService.selectCreditEcheances(credit_id).stream()
                    .flatMap(echeance -> this.getEcheanceIncidents(echeance.getId()).stream())
                    .sorted((inc1, inc2) -> inc1.getDateIncident().compareTo(inc2.getDateIncident()))
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public List<Incident> getPersonIncidents(Integer person_id) {
        if (person_id == null) throw new  IllegalArgumentException("L'id incident ne peut pas être null");
        try {
            Person person = personRepository.findPerson(person_id).orElseThrow(RuntimeException::new);
            return echeanceService.selectPersonEcheances(person_id).stream()
                    .flatMap(echeance -> this.getEcheanceIncidents(echeance.getId()).stream())
                    .sorted((incident1, incident2) -> incident1.getDateIncident().compareTo(incident2.getDateIncident()))
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new DatabaseException(e.getMessage(), e);
        }
    }
}
