-- Enums based on scoring system requirements
CREATE TYPE decision_enum AS ENUM ('ACCORD_IMMEDIAT', 'ETUDE_MANUELLE', 'REFUS_AUTOMATIQUE');
CREATE TYPE statut_paiement_enum AS ENUM ('PAYE_A_TEMPS', 'EN_RETARD', 'PAYE_EN_RETARD', 'IMPAYE_NON_REGLE', 'IMPAYE_REGLE');
CREATE TYPE typeincident_enum AS ENUM ('PAYE_A_TEMPS', 'EN_RETARD', 'PAYE_EN_RETARD', 'IMPAYE_NON_REGLE', 'IMPAYE_REGLE');
CREATE TYPE typecontrat_enum AS ENUM ('CDI_SECTEUR_PUBLIC', 'CDI_SECTEUR_PRIVE_GRANDE_ENTREPRISE', 'CDI_SECTEUR_PRIVE_PME', 'CDD_INTERIM', 'PROFESSION_LIBERALE_STABLE', 'AUTO_ENTREPRENEUR');
CREATE TYPE situation_familiale_enum AS ENUM ('MARIE','CELIBATAIRE');

-- Personne (base class)
CREATE TABLE personne (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255) NOT NULL,
    date_de_naissance DATE,
    ville VARCHAR(255),
    nombre_enfants INTEGER DEFAULT 0,
    investissement BOOLEAN DEFAULT FALSE,
    placement BOOLEAN DEFAULT FALSE,
    situation_familiale situation_familiale_enum,
    created_at TIMESTAMPTZ DEFAULT now(),
    score INTEGER DEFAULT 0
);

-- Employe (subclass)
CREATE TABLE employe (
    personne_id INTEGER PRIMARY KEY REFERENCES personne(id) ON DELETE CASCADE,
    salaire NUMERIC(14,2),
    anciennete INTEGER,
    poste VARCHAR(255),
    type_contrat typecontrat_enum
);

-- Professionnel (subclass)
CREATE TABLE professionnel (
    personne_id INTEGER PRIMARY KEY REFERENCES personne(id) ON DELETE CASCADE,
    revenu NUMERIC(14,2),
    immatriculation_fiscale VARCHAR(255),
    secteur_activite VARCHAR(50),
    activite VARCHAR(255),
    type_contrat typecontrat_enum
);

-- Credit
CREATE TABLE credit (
    id SERIAL PRIMARY KEY,
    personne_id INTEGER REFERENCES personne(id) ON DELETE SET NULL,
    date_de_credit DATE,
    montant_demande NUMERIC(14,2),
    montant_octroye NUMERIC(14,2),
    taux_interet NUMERIC(6,4), -- e.g. 0.0450 for 4.5%
    duree_en_mois INTEGER,
    type_credit VARCHAR(50),
    decision decision_enum
);

-- Echeance
CREATE TABLE echeance (
    id SERIAL PRIMARY KEY,
    credit_id INTEGER REFERENCES credit(id) ON DELETE CASCADE,
    date_echeance DATE,
    mensualite NUMERIC(14,2),
    date_de_paiement DATE,
    statut_paiement statut_paiement_enum
);

-- Incident
CREATE TABLE incident (
    id SERIAL PRIMARY KEY,
    date_incident DATE,
    echeance_id INTEGER REFERENCES echeance(id) ON DELETE SET NULL,
    score INTEGER,
    type_incident typeincident_enum
);

-- Indexes for FK lookups (optional)
CREATE INDEX idx_credit_personne ON credit(personne_id);
CREATE INDEX idx_echeance_credit ON echeance(credit_id);
CREATE INDEX idx_incident_echeance ON incident(echeance_id);