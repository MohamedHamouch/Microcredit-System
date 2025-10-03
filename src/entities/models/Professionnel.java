package entities.models;

import entities.enums.TypeContrat;

public class Professionnel extends Personne {
    private Double revenu;
    private String immatriculationFiscale;
    private String secteurActivite;
    private String Activite;
    private TypeContrat typeContrat;

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

    public String getSecteurActivite() {
        return secteurActivite;
    }

    public void setSecteurActivite(String secteurActivite) {
        this.secteurActivite = secteurActivite;
    }

    public String getActivite() {
        return Activite;
    }

    public void setActivite(String activite) {
        Activite = activite;
    }

    public TypeContrat getTypeContrat() {
        return typeContrat;
    }

    public void setTypeContrat(TypeContrat typeContrat) {
        this.typeContrat = typeContrat;
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(" - Professionnel: %s (%s), Revenu: %.2f DH, Contrat: %s", 
            Activite, secteurActivite, revenu, typeContrat.getDescription());
    }
}
