package entities.models;

import entities.enums.StatutPaiement;
import java.time.LocalDate;

public class Echeance {
    private Integer id;
    private Integer creditId; 
    private LocalDate dateEcheance;
    private LocalDate dateDePaiment;
    private Double mensualite;
    private StatutPaiement statutPaiement;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCreditId() {
        return creditId;
    }

    public void setCreditId(Integer creditId) {
        this.creditId = creditId;
    }

    public Double getMensualite() {
        return mensualite;
    }

    public void setMensualite(Double mensualite) {
        this.mensualite = mensualite;
    }

    public LocalDate getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(LocalDate dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public LocalDate getDateDePaiment() {
        return dateDePaiment;
    }

    public void setDateDePaiment(LocalDate dateDePaiment) {
        this.dateDePaiment = dateDePaiment;
    }

    public StatutPaiement getStatutPaiement() {
        return statutPaiement;
    }

    public void setStatutPaiement(StatutPaiement statutPaiement) {
        this.statutPaiement = statutPaiement;
    }
}
