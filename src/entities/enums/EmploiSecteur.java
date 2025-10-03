package entities.enums;

public enum EmploiSecteur {
    PUBLIC("Public"),
    GRANDE_ENTREPRISE("Grande entreprise"),
    PME("PME");

    private final String label;
    EmploiSecteur(String label) { this.label = label; }
    public String getLabel() { return label; }
}
