package entities.models;

import entities.enums.Decision;
import entities.enums.TypeCredit;

import java.time.LocalDate;

public class Credit {
    private Integer id;
    private LocalDate dateCredit;
    private Dobule mantantDemande;
    private Dobule montantCtroye;
    private Dobule tauxInteret;
    private Integer dureeEnMois;
    private TypeCredit typeCredit;
    private Decision decision;

    public LocalDate getDateCredit() {
        return dateCredit;
    }

    public void setDateCredit(LocalDate dateCredit) {
        this.dateCredit = dateCredit;
    }

    public Dobule getMantantDemande() {
        return mantantDemande;
    }

    public void setMantantDemande(Dobule mantantDemande) {
        this.mantantDemande = mantantDemande;
    }

    public Dobule getMontantCtroye() {
        return montantCtroye;
    }

    public void setMontantCtroye(Dobule montantCtroye) {
        this.montantCtroye = montantCtroye;
    }

    public Dobule getTauxInteret() {
        return tauxInteret;
    }

    public void setTauxInteret(Dobule tauxInteret) {
        this.tauxInteret = tauxInteret;
    }

    public Integer getDureeEnMois() {
        return dureeEnMois;
    }

    public void setDureeEnMois(Integer dureeEnMois) {
        this.dureeEnMois = dureeEnMois;
    }

    public TypeCredit getTypeCredit() {
        return typeCredit;
    }

    public void setTypeCredit(TypeCredit typeCredit) {
        this.typeCredit = typeCredit;
    }

    public Decision getDecision() {
        return decision;
    }

    public void setDecision(Decision decision) {
        this.decision = decision;
    }
}
