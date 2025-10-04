package entities.models;

import entities.enums.SecteurActivite;

public class Professionnel {
    private Double revenu;
    private String immatriculationFiscale;
    private SecteurActivite secteurActivite;
    private String Activite;

    public Double getRevenu() {
        return revenu;
    }

    public void setRevenu(Double revenu) {
        this.revenu = revenu;
    }

    public String getImmatriculationFiscale() {
        return immatriculationFiscale;
    }

    public void setImmatriculationFiscale(String immatriculationFiscale) {
        this.immatriculationFiscale = immatriculationFiscale;
    }

    public SecteurActivite getSecteurActivite() {
        return secteurActivite;
    }

    public void setSecteurActivite(SecteurActivite secteurActivite) {
        this.secteurActivite = secteurActivite;
    }

    public String getActivite() {
        return Activite;
    }

    public void setActivite(String activite) {
        Activite = activite;
    }
}
