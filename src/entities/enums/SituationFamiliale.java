package entities.enums;

public enum SituationFamiliale {
    MARIE("Marié"),
    CELIBATAIRE("Célibataire");

    private final String label;
    SituationFamiliale(String label) { this.label = label; }
    public String getLabel() { return label; }
}
