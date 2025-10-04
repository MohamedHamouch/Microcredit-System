package main.service.interfaces;

import main.model.Incident;

import java.util.List;
import java.util.Optional;

public interface IncidentService {
    Incident insertIncident(Incident incident);
    Incident findIncident(Integer id);
    List<Incident> getAllIncidents();
    List<Incident> getEcheanceIncidents(Integer echeance_id);
    List<Incident> getCreditIncidents(Integer credit_id);
    List<Incident> getPersonIncidents(Integer person_id);
}
