package entities.models;

import entities.enums.SituationFamiliale;
import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class Personne {
    private Integer id;
    private String nom;
    private String prenom;
    private LocalDate dateDeNaissance;
    private String ville;
    private Integer nombreEnfants;
    private Boolean investissement;
    private Boolean placement;
    private LocalDateTime createdAt;
    private Integer score;
    private SituationFamiliale situationFamiliale;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getDateDeNaissance() {
        return dateDeNaissance;
    }

    public void setDateDeNaissance(LocalDate dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public Integer getNombreEnfants() {
        return nombreEnfants;
    }

    public void setNombreEnfants(Integer nombreEnfants) {
        this.nombreEnfants = nombreEnfants;
    }

    public Boolean isInvestissement() {
        return investissement;
    }

    public void setInvestissement(Boolean investissement) {
        this.investissement = investissement;
    }

    public Boolean isPlacement() {
        return placement;
    }

    public void setPlacement(Boolean placement) {
        this.placement = placement;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public SituationFamiliale getSituationFamiliale() {
        return situationFamiliale;
    }

    public void setSituationFamiliale(SituationFamiliale situationFamiliale) {
        this.situationFamiliale = situationFamiliale;
    }
    
    @Override
    public String toString() {
        String situationLabel = situationFamiliale != null ? situationFamiliale.getLabel() : "Non définie";
        return String.format("%s %s (ID: %d) - %s, %s - Score: %d - Enfants: %d", 
            prenom != null ? prenom : "", 
            nom != null ? nom : "", 
            id != null ? id : 0, 
            ville != null ? ville : "", 
            situationLabel, 
            score != null ? score : 0, 
            nombreEnfants != null ? nombreEnfants : 0);
    }
}
