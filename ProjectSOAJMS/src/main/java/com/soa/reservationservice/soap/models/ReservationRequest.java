package com.soa.reservationservice.soap.models;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "ReservationRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReservationRequest {
    @XmlElement(required = true) private String matricule;
    @XmlElement(required = true) private int dureeJours;
    @XmlElement(defaultValue = "Basique") private String niveauAssurance;
    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule =
            matricule; }
    public int getDureeJours() { return dureeJours; }
    public void setDureeJours(int dureeJours) { this.dureeJours =
            dureeJours; }
    public String getNiveauAssurance() { return niveauAssurance; }
    public void setNiveauAssurance(String niveauAssurance) {
        this.niveauAssurance = niveauAssurance; }
}