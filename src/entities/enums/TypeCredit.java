package entities.enums;

public enum TypeCredit {
    CONSO("Consommation"),
    IMMOBILIER("Immobilier"),
    AUTO("Auto"),
    PERSONNEL("Personnel");

    private final String label;
    TypeCredit(String label) { this.label = label; }
    public String getLabel() { return label; }
}
