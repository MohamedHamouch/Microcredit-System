package services;

import entities.enums.*;
import entities.models.Employe;
import entities.models.Personne;
import entities.models.Professionnel;
import java.time.LocalDate;
import java.time.Period;

public class ScoringService {

    public static int calculateScore(Personne personne) {
        if (personne == null) return 0;
        
        int score = 0;
        
        if (personne instanceof Employe) {
            Employe employe = (Employe) personne;
            
            score += calculateEmployeeStabilityScore(employe);
            
            score += calculateFinancialCapacityScore(employe.getSalaire());
            
        } else if (personne instanceof Professionnel) {
            Professionnel professionnel = (Professionnel) personne;
            
            score += calculateProfessionalStabilityScore(professionnel);
            
            score += calculateFinancialCapacityScore(professionnel.getRevenu());
        }
        
        score += calculateClientRelationScore(personne);
        
        score += calculatePatrimoineScore(personne);
        
        return score;
    }
    
    private static int calculateEmployeeStabilityScore(Employe employe) {
        int stabilityScore = 0;
        
        if (employe.getTypeContrat() == TypeContrat.CDI_SECTEUR_PUBLIC) {
            stabilityScore += 25;
        } else if (employe.getTypeContrat() == TypeContrat.CDI_SECTEUR_PRIVE_GRANDE_ENTREPRISE) {
            stabilityScore += 15;
        } else if (employe.getTypeContrat() == TypeContrat.CDI_SECTEUR_PRIVE_PME) {
            stabilityScore += 12;
        } else if (employe.getTypeContrat() == TypeContrat.CDD_INTERIM) {
            stabilityScore += 10;
        }
        
        if (employe.getAnciennete() >= 5) {
            stabilityScore += 5;
        } else if (employe.getAnciennete() >= 2) {
            stabilityScore += 3;
        } else if (employe.getAnciennete() >= 1) {
            stabilityScore += 1;
        }
        
        return stabilityScore;
    }
    
    private static int calculateProfessionalStabilityScore(Professionnel professionnel) {
        int stabilityScore = 0;
        
        if (professionnel.getTypeContrat() == TypeContrat.PROFESSION_LIBERALE_STABLE) {
            stabilityScore += 18;
        } else if (professionnel.getTypeContrat() == TypeContrat.AUTO_ENTREPRENEUR) {
            stabilityScore += 12;
        }
        
        return stabilityScore;
    }
    
    private static int calculateFinancialCapacityScore(Double revenu) {
        if (revenu == null) return 0;
        
        if (revenu >= 10000) {
            return 30;
        } else if (revenu >= 8000) {
            return 25;
        } else if (revenu >= 5000) {
            return 20;
        } else if (revenu >= 3000) {
            return 15;
        } else {
            return 10;
        }
    }
    
    private static int calculateClientRelationScore(Personne personne) {
        int relationScore = 0;
        
        if (personne.getDateDeNaissance() != null) {
            int age = Period.between(personne.getDateDeNaissance(), LocalDate.now()).getYears();
            if (age >= 18 && age <= 25) {
                relationScore += 4;
            } else if (age >= 26 && age <= 35) {
                relationScore += 8;
            } else if (age >= 36 && age <= 55) {
                relationScore += 10;
            } else if (age > 55) {
                relationScore += 6;
            }
        }
        
        if (personne.getSituationFamiliale() == SituationFamiliale.MARIE) {
            relationScore += 3;
        } else if (personne.getSituationFamiliale() == SituationFamiliale.CELIBATAIRE) {
            relationScore += 2;
        }
        
        int enfants = personne.getNombreEnfants() != null ? personne.getNombreEnfants() : 0;
        if (enfants == 0) {
            relationScore += 2;
        } else if (enfants <= 2) {
            relationScore += 1;
        }
        
        return relationScore;
    }
    
    private static int calculatePatrimoineScore(Personne personne) {
        int patrimoineScore = 0;
        
        if (personne.isInvestissement() != null && personne.isInvestissement()) {
            patrimoineScore += 10;
        }
        
        if (personne.isPlacement() != null && personne.isPlacement()) {
            patrimoineScore += 10;
        }
        
        return patrimoineScore;
    }
    
}