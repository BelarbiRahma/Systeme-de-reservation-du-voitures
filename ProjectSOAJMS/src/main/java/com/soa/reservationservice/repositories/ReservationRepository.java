package com.soa.reservationservice.repositories;
import com.soa.reservationservice.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ReservationRepository extends JpaRepository<Reservation,
        Long> {
    List<Reservation> findByClientIdClient(Long clientId);
}