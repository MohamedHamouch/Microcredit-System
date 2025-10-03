package entities.enums;

public enum TypeContrat {
    CDI_SECTEUR_PUBLIC("CDI secteur public"),
    CDI_SECTEUR_PRIVE_GRANDE_ENTREPRISE("CDI secteur privé (grande entreprise)"),
    CDI_SECTEUR_PRIVE_PME("CDI secteur privé (PME)"),
    CDD_INTERIM("CDD/Intérim"),
    PROFESSION_LIBERALE_STABLE("Profession libérale stable"),
    AUTO_ENTREPRENEUR("Auto-entrepreneur");

    private final String description;
    TypeContrat(String description) { this.description = description; }
    public String getDescription() { return description; }
}
