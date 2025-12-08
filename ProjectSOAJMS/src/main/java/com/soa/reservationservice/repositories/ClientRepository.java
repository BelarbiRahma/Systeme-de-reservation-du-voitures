package com.soa.reservationservice.repositories;
import com.soa.reservationservice.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ClientRepository extends JpaRepository<Client, Long> {}