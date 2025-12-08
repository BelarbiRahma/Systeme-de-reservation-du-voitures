package com.soa.reservationservice.repositories;
import com.soa.reservationservice.entities.Voiture;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface VoitureRepository extends JpaRepository<Voiture, String> {
    List<Voiture> findByStatutDisponibilite(String statut);

}
