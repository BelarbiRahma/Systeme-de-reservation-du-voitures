package com.soa.reservationservice.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.io.Serializable;
@Entity
public class Voiture implements Serializable {
    @Id
    private String matricule;
    private String marque;
    private String modele;
    private double prixJour;
    private String statutDisponibilite = "DISPONIBLE";
    public Voiture() {}
    public Voiture(String matricule, String marque, String modele, double
            prixJour, String statut) {
        this.matricule = matricule; this.marque = marque; this.modele =
                modele;
        this.prixJour = prixJour; this.statutDisponibilite = statut;
    }
    // getters and setters
    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule =
            matricule; }
    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }
    public String getModele() { return modele; }
    public void setModele(String modele) { this.modele = modele; }
    public double getPrixJour() { return prixJour; }
    public void setPrixJour(double prixJour) { this.prixJour = prixJour; }
    public String getStatutDisponibilite() { return statutDisponibilite; }
    public void setStatutDisponibilite(String statutDisponibilite) {
        this.statutDisponibilite = statutDisponibilite; }
}