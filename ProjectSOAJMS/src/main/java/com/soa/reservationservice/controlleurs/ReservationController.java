package com.soa.reservationservice.controlleurs;
import com.soa.reservationservice.entities.Reservation;
import com.soa.reservationservice.entities.Voiture;
import com.soa.reservationservice.repositories.ReservationRepository;
import com.soa.reservationservice.repositories.VoitureRepository;
import com.soa.reservationservice.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.temporal.ChronoUnit;
import java.util.List;
@RestController
@RequestMapping("/api/reservations")
public class    ReservationController {
    @Autowired private ReservationRepository reservationRepository;
    @Autowired private VoitureRepository voitureRepository;
    @Autowired private ClientRepository clientRepository;
    @PostMapping
    public ResponseEntity<Reservation> create(@RequestBody Reservation
                                                      reservation) {
        String matricule = reservation.getVoiture().getMatricule();
        Voiture v = voitureRepository.findById(matricule).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Voiture non trouvée"));
        if (!"DISPONIBLE".equalsIgnoreCase(v.getStatutDisponibilite()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Voiture non disponible");
        long days = ChronoUnit.DAYS.between(reservation.getDateDebut(),
                reservation.getDateFin()) + 1;
        reservation.setMontantTotal(days * v.getPrixJour());
        if (reservation.getClient().getIdClient() == null)
            clientRepository.save(reservation.getClient());
        v.setStatutDisponibilite("RESERVE"); voitureRepository.save(v);
        Reservation saved = reservationRepository.save(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    @GetMapping
    public List<Reservation> getAll(){ return
            reservationRepository.findAll(); }
    @GetMapping("/client/{clientId}")
    public List<Reservation> getByClient(@PathVariable Long clientId){
        return reservationRepository.findByClientIdClient(clientId); }
}