package com.soa.clientfx.services;

public class SoapClientService {

    public double calculerFraisAssurance(String matricule, int dureeJours, String niveauAssurance) {
        // Simulation simple pour éviter les dépendances complexes
        double tarifBase = switch (niveauAssurance.toLowerCase()) {
            case "basique" -> 15.0;
            case "complet" -> 25.0;
            case "premium" -> 40.0;
            default -> 15.0;
        };

        double cout = tarifBase * dureeJours;
        double tva = cout * 0.10;
        return cout + tva;
    }
}