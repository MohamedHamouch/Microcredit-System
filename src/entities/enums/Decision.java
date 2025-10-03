package entities.enums;

public enum Decision {
    ACCORD_IMMEDIAT("Accord immédiat"),
    ETUDE_MANUELLE("Étude manuelle"),
    REFUS_AUTOMATIQUE("Refus automatique");

    private final String message;

    Decision(String message) { this.message = message; }

    public String getMessage() { return message; }
}
