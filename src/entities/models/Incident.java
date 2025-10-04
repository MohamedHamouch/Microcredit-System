package entities.models;

import entities.enums.TypeIncident;

import java.time.LocalDate;

public class Incident {
    private Integer id;
    private LocalDate dateIncident;
    private Echeance echeance;
    private Integer score;
    private TypeIncident typeIncident;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDateIncident() {
        return dateIncident;
    }

    public void setDateIncident(LocalDate dateIncident) {
        this.dateIncident = dateIncident;
    }

    public Echeance getEcheance() {
        return echeance;
    }

    public void setEcheance(Echeance echeance) {
        this.echeance = echeance;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public TypeIncident getTypeIncident() {
        return typeIncident;
    }

    public void setTypeIncident(TypeIncident typeIncident) {
        this.typeIncident = typeIncident;
    }
}
