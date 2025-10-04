package entities.models;

import entities.enums.StatusPaiment;

import java.time.LocalDate;

public class Echeance {
    private Integer id;
    private LocalDate dateEcheance;
    private Double name;
    private StatusPaiment statusPaiement;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getMensualite() {
        return mensualite;
    }

    public void setMensualite(Double mensualite) {
        this.mensualite = mensualite;
    }

    public LocalDate getDateDePaiment() {
        return dateDePaiment;
    }

    public void setDateDePaiment(LocalDate dateDePaiment) {
        this.dateDePaiment = dateDePaiment;
    }

    public StatusPaiment getStatusPaiement() {
        return statusPaiement;
    }

    public void setStatusPaiement(StatusPaiment statusPaiement) {
        this.statusPaiement = statusPaiement;
    }
}
