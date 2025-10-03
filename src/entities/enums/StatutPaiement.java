package entities.enums;

public enum StatutPaiement {
    PAYE_A_TEMPS("Payé à temps"),
    EN_RETARD("En retard"),
    PAYE_EN_RETARD("Payé en retard"),
    IMPAYE_NON_REGLE("Impaye non réglé"),
    IMPAYE_REGLE("Impaye réglé");

    private final String message;
    StatutPaiement(String message) { this.message = message; }
    public String getMessage() { return message; }
}
