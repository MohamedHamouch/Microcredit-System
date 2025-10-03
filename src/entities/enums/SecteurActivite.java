package entities.enums;

public enum SecteurActivite {
    AGRICULTURE("Agriculture"),
    SERVICE("Service"),
    COMMERCE("Commerce"),
    CONSTRUCTION("Construction"),
    AUTRE("Autre");

    private final String label;
    SecteurActivite(String label) { this.label = label; }
    public String getLabel() { return label; }
}
