package entities.enums;

public enum TypeIncident {
    PAYE_A_TEMPS("Payé à temps"),
    EN_RETARD("En retard"),
    PAYE_EN_RETARD("Payé en retard"),
    IMPAYE_NON_REGLE("Impayé non réglé"),
    IMPAYE_REGLE("Impayé réglé");

    private final String message;
    TypeIncident(String message) { this.message = message; }
    public String getMessage() { return message; }
}
