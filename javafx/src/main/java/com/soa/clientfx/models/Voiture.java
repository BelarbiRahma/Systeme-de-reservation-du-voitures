package com.soa.clientfx.models;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Voiture {
    private final SimpleStringProperty matricule;
    private final SimpleStringProperty marque;
    private final SimpleStringProperty modele;
    private final SimpleDoubleProperty prixJour;
    private final SimpleStringProperty statut;

    public Voiture() {
        this("", "", "", 0.0, "");
    }

    public Voiture(String matricule, String marque, String modele,
                   double prixJour, String statut) {
        this.matricule = new SimpleStringProperty(matricule);
        this.marque = new SimpleStringProperty(marque);
        this.modele = new SimpleStringProperty(modele);
        this.prixJour = new SimpleDoubleProperty(prixJour);
        this.statut = new SimpleStringProperty(statut);
    }

    // Getters
    public String getMatricule() { return matricule.get(); }
    public String getMarque() { return marque.get(); }
    public String getModele() { return modele.get(); }
    public double getPrixJour() { return prixJour.get(); }
    public String getStatut() { return statut.get(); }

    // Setters
    public void setMatricule(String matricule) { this.matricule.set(matricule); }
    public void setMarque(String marque) { this.marque.set(marque); }
    public void setModele(String modele) { this.modele.set(modele); }
    public void setPrixJour(double prixJour) { this.prixJour.set(prixJour); }
    public void setStatutDisponibilite(String statut) { this.statut.set(statut); }
    public void setStatut(String statut) { this.statut.set(statut); }
}