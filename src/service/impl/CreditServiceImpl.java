package main.service.impl;

import main.enums.EnumDecision;
import main.enums.StatutPaiement;
import main.model.Credit;
import main.model.Echeance;
import main.model.Employe;
import main.model.Person;
import main.repository.interfaces.*;
import main.service.interfaces.CreditService;
import main.service.interfaces.EcheanceService;
import main.service.interfaces.EmployeService;
import main.service.interfaces.ProfessionnelService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CreditServiceImpl implements CreditService {
    private final CreditRepository creditRepository;
    private final PersonRepository personRepository;
    private final EmployeService employeService;
    private final ProfessionnelService professionnelService;
    private final EcheanceRepository echeanceRepository;

    public CreditServiceImpl(CreditRepository creditRepository, PersonRepository personRepository, EmployeService employeService, ProfessionnelService professionnelService, EcheanceRepository echeanceRepository) {
        this.creditRepository = creditRepository;
        this.personRepository = personRepository;
        this.employeService = employeService;
        this.professionnelService = professionnelService;
        this.echeanceRepository = echeanceRepository;
    }

    @Override
    public Credit ajouterCredit(Credit credit) {
        if (credit == null) throw new IllegalArgumentException("Les information de credit ne peut pas être null");
        try {
            Credit finalCredit = credit;
            Person person = personRepository.findPerson(credit.getPerson_id()).orElseThrow(() -> new RuntimeException("Aucun client trouvé avec l'id: " + finalCredit.getPerson_id()));
            Double salaire = this.touverSalaire(person.getRole().getDescription(), credit.getPerson_id());
            List<Credit> credits = this.getPersonCredits(person.getId()).stream()
                    .filter(credit1 -> !credit1.getDecision().equals(EnumDecision.REFUS_AUTOMATIQUE))
                    .collect(Collectors.toList());
            List<Echeance> echeancesPerson = this.getPersonCredits(person.getId()).stream()
                    .flatMap(credit1 -> this.selectCreditEcheances(credit1.getId()).stream())
                    .filter(cr -> cr.getStatutPaiement().equals(StatutPaiement.IMPAYENONREGLE)
                            || cr.getStatutPaiement().equals(StatutPaiement.ENRETARD)
                            || cr.getStatutPaiement().equals(StatutPaiement.PENDING))
                    .collect(Collectors.toList());
            if (!echeancesPerson.isEmpty()) throw new RuntimeException("Vous ne pouvez pas obtenir de nouveau crédit car il a " + echeancesPerson.size() + " échéance(s) impayée(s).");
            String existence = credits.isEmpty() ? "nouveau" : "existant";
            Integer score = person.getScore();
            switch (existence) {
                case "nouveau":
                    if (score >= 70) {
                        if (credit.getMontantDemande() > 4 * salaire) credit.setMontantOctroye(4 * salaire);
                        credit.setDecision(EnumDecision.ETUDEMANUELLE);
                    } else  {
                        credit.setDecision(EnumDecision.REFUS_AUTOMATIQUE);
                    }
                    break;

                case "existant":
                    if (score >= 80) {
                        credit.setDecision(EnumDecision.ACCORDIMMEDIAT);
                        if (credit.getMontantDemande() > 10 * salaire) {
                            credit.setMontantOctroye(10 * salaire);
                            credit.setDecision(EnumDecision.ETUDEMANUELLE);
                        }
                    } else if (score >= 60) {
                        if (credit.getMontantDemande() > 7 * salaire) credit.setMontantOctroye(7 * salaire);
                        credit.setDecision(EnumDecision.ETUDEMANUELLE);
                    } else {
                        credit.setDecision(EnumDecision.REFUS_AUTOMATIQUE);
                    }
                    break;
            }

            credit.generatedDureeMois(credit.getMontantOctroye());

            credit = creditRepository.insertCredit(credit).orElseThrow(() -> new RuntimeException("Impossiple de l'insertion ce credit"));
            if (!credit.getDecision().equals(EnumDecision.REFUS_AUTOMATIQUE)) {
                Double montant = credit.getMontantOctroye() + credit.getMontantOctroye() * credit.getTauxInteret();
                Double mensualite = ((int) (montant / credit.getDureeenMois())) + 1.;
                for (int i = 1; i <= credit.getDureeenMois(); i++) {
                    Echeance echeance = new Echeance(credit.getDateCredit().plusMonths(i), mensualite, null, StatutPaiement.PENDING, credit.getId());
                    echeance = echeanceRepository.insertEcheance(echeance)
                            .orElseThrow(() -> new RuntimeException("Impossible d'ajouter d'echeance"));
                }
            }
            return credit;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Credit findCredit(Integer id) {
        if (id == null) throw new  IllegalArgumentException("L'id credit ne peut pas être null");
        try {
            return creditRepository.findCredit(id)
                    .orElseThrow((() -> new RuntimeException("Aucun credit trouvé avec l'id: " + id)));
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Credit updateCredit(Integer id, Map<String, Object> update) {
        if (id == null) throw new  IllegalArgumentException("L'id credit ne peut pas être null");
        if (update.isEmpty()) throw new  RuntimeException("Les modifications ne peut pas être vide");
        try {
            Credit credit = this.findCredit(id);
            return creditRepository.updateCredit(credit, update)
                    .orElseThrow(() -> new RuntimeException("Impossible de modifier le credit d'id: " + id));
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public List<Credit> getAllCredits() {
        try {
            return creditRepository.selectCredits().stream()
                    .sorted((cd1, cd2) -> {
                        return cd1.getDateCredit().compareTo(cd2.getDateCredit());
                    })
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Boolean deleteCredit(Integer id) {
        if (id == null) throw new  IllegalArgumentException("L'id de credit ne peut pas être null");
        try {
            Credit credit = this.findCredit(id);
            return creditRepository.deleteCredit(credit);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public List<Credit> getPersonCredits(Integer person_id) {
        if (person_id == null) throw new  IllegalArgumentException("L'id credit ne peut pas être null");
        try {
            Person person = personRepository.findPerson(person_id).orElseThrow(() -> new RuntimeException("Aucun client trouvé avec l'id: " + person_id));
            return creditRepository.selectPersonCredits(person_id).stream()
                    .sorted((cd1, cd2) -> {
                        return cd1.getDateCredit().compareTo(cd2.getDateCredit());
                    })
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private Double touverSalaire(String role, Integer person_id) {
        try {
            if (role.equals("employé")) {
                return this.employeService.findEmploye(person_id).getSalaire();
            } else if (role.equals("professionnel")) {
                return this.professionnelService.findProfessionnel(person_id).getRevenu();
            }
            return 0.;
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public List<Echeance> selectCreditEcheances(Integer id) {
        if (id == null) throw new  IllegalArgumentException("L'id de credit ne peut pas être null");
        try {
            Credit credit = this.findCredit(id);
            return echeanceRepository.selectCreditEcheances(id).stream()
                    .sorted((ech1, ech2) -> ech1.getDateEcheance().compareTo(ech2.getDateEcheance()))
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
