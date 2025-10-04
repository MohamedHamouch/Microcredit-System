package main.repository.interfaces;

import main.model.Professionnel;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProfessionnelRepository {
    Optional<Professionnel> insertProfessionnel(Professionnel professionnel);
    Optional<Professionnel> findProfessionnel(Integer id);
    Optional<Professionnel> updateProfessionnel(Professionnel prof, Map<String, Object> updates);
    List<Professionnel> selectProfessionnels();
}
