package main.service.interfaces;

import main.model.Professionnel;

import java.util.List;
import java.util.Map;

public interface ProfessionnelService {
    Professionnel ajouterProfessionnel(Professionnel professionnel);
    Professionnel findProfessionnel(Integer id);
    Professionnel updateProfessionnel(Integer id, Map<String, Object> update);
    List<Professionnel> getAllProfessionnels();
    Boolean deleteProfessionnel(Integer id);
}
