package main.repository.interfaces;

import main.model.Incident;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IncidentRepository {
    Optional<Incident> insertIncident(Incident incident);
    Optional<Incident> findIncident(Integer id);
    List<Incident> selectIncidents();
    List<Incident> selectEcheanceIncidents(Integer id);
}
