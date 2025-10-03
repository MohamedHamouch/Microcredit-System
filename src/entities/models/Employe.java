package entities.models;

import entities.enums.TypeContrat;

public class Employe extends Personne {
    private Double salaire;
    private Integer anciennete;
    private String poste;
    private TypeContrat typeContrat;

    public Double getSalaire() {
        return salaire;
    }

    public void setSalaire(Double salaire) {
        this.salaire = salaire;
    }

    public Integer getAnciennete() {
        return anciennete;
    }

    public void setAnciennete(Integer anciennete) {
        this.anciennete = anciennete;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public TypeContrat getTypeContrat() {
        return typeContrat;
    }

    public void setTypeContrat(TypeContrat typeContrat) {
        this.typeContrat = typeContrat;
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(" - Employé: %s, Salaire: %.2f DH, Ancienneté: %d ans, Contrat: %s", 
            poste, salaire, anciennete, typeContrat.getDescription());
    }
}
