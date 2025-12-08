package com.soa.clientfx.models;

import java.time.LocalDate;

public class Reservation {
    private final String matricule;
    private final String clientNom;
    private final LocalDate dateDebut;
    private final LocalDate dateFin;
    private final double montant;
    private final String niveauAssurance;

    // Constructor, getters, toString()...
    public Reservation(String matricule, String clientNom, LocalDate dateDebut,
                       LocalDate dateFin, double montant, String niveauAssurance) {
        this.matricule = matricule;
        this.clientNom = clientNom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.montant = montant;
        this.niveauAssurance = niveauAssurance;
    }

    // Pattern matching dans toString (Java 17+)
    @Override
    public String toString() {
        return String.format("""
            Réservation:
            - Voiture: %s
            - Client: %s
            - Période: %s à %s
            - Assurance: %s
            - Total: %.2f €
            """, matricule, clientNom, dateDebut, dateFin,
                switch(niveauAssurance) {
                    case "Basique" -> "Basique";
                    case "Complet" -> "Complète";
                    case "Premium" -> "Premium";
                    default -> "Standard";
                }, montant);
    }
}