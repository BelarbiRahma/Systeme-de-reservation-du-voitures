package com.soa.reservationservice.entities;
import jakarta.persistence.*;
import java.io.Serializable;
@Entity
public class Client implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idClient;
    private String nom;
    @Column(unique = true)
    private String email;
    public Client() {}
    public Client(String nom, String email) { this.nom = nom; this.email =
            email; }
    // getters/setters
    public Long getIdClient() { return idClient; }
    public void setIdClient(Long idClient) { this.idClient = idClient; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}