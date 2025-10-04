package main.service.impl;

import main.model.Professionnel;
import main.repository.interfaces.PersonRepository;
import main.repository.interfaces.ProfessionnelRepository;
import main.service.interfaces.ProfessionnelService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProfessionnelServiceImpl implements ProfessionnelService {
    private final ProfessionnelRepository professionnelRepository;
    private final PersonRepository personRepository;

    public ProfessionnelServiceImpl(ProfessionnelRepository professionnelRepository, PersonRepository personRepository) {
        this.professionnelRepository = professionnelRepository;
        this.personRepository = personRepository;
    }

    @Override
    public Professionnel ajouterProfessionnel(Professionnel professionnel) {
        if (professionnel == null) throw new IllegalArgumentException("Les information de person ne peut pas être null");
        try {
            return professionnelRepository.insertProfessionnel(professionnel)
                    .orElseThrow(() -> new RuntimeException("Impossible d'ajouter l'professionnel: " + professionnel.getNom() + professionnel.getPrenom()));
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Professionnel findProfessionnel(Integer id) {
        if (id == null) throw new  IllegalArgumentException("L'id de person ne peut pas être null");
        try {
            return professionnelRepository.findProfessionnel(id)
                    .orElseThrow((() -> new RuntimeException("Aucun professionnel trouvé avec l'id: " + id)));
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Professionnel updateProfessionnel(Integer id, Map<String, Object> update) {
        if (id == null) throw new  IllegalArgumentException("L'id de person ne peut pas être null");
        if (update.isEmpty()) throw new  RuntimeException("Les modifications ne peut pas être vide");
        try {
            Professionnel professionnel = this.findProfessionnel(id);
            return professionnelRepository.updateProfessionnel(professionnel, update)
                    .orElseThrow(() -> new RuntimeException("Impossible de modifier l'professionnel d'id: " + id));
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public List<Professionnel> getAllProfessionnels() {
        try {
            return professionnelRepository.selectProfessionnels().stream()
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
    public Boolean deleteProfessionnel(Integer id) {
        if (id == null) throw new  IllegalArgumentException("L'id de person ne peut pas être null");
        try {
            Professionnel professionnel = this.findProfessionnel(id);
            return personRepository.deletePerson(professionnel);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
