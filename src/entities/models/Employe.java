package entities.models;

import entities.enums.EmploiSecteur;
import entities.enums.TypeContrat;

public class Employe {
    private Double salaire;
    private Integer anciennete;
    private String poste;
    private TypeContrat typeContrat;
    private EmploiSecteur secteur;

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

    public EmploiSecteur getSecteur() {
        return secteur;
    }

    public void setSecteur(EmploiSecteur secteur) {
        this.secteur = secteur;
    }
}
