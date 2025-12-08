package com.soa.reservationservice.controlleurs;
import com.soa.reservationservice.entities.Voiture;
import com.soa.reservationservice.repositories.VoitureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
@RestController
@RequestMapping("/api/voitures")
public class VoitureController {
    @Autowired
    private VoitureRepository voitureRepository;
    @GetMapping
    public List<Voiture> getAllVoitures() { return
            voitureRepository.findByStatutDisponibilite("DISPONIBLE"); }
    @PostMapping
    public ResponseEntity<Voiture> createVoiture(@RequestBody Voiture
                                                         voiture) {
        Voiture saved = voitureRepository.save(voiture);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    @PutMapping("/{matricule}")
    public Voiture updateVoiture(@PathVariable String matricule,
                                 @RequestBody Voiture newV){
        return voitureRepository.findById(matricule)
                .map(v-> { v.setMarque(newV.getMarque());
                    v.setModele(newV.getModele()); v.setPrixJour(newV.getPrixJour());
                    v.setStatutDisponibilite(newV.getStatutDisponibilite()); return
                            voitureRepository.save(v); })
                .orElseThrow(()-> new
                        ResponseStatusException(HttpStatus.NOT_FOUND, "Voiture non trouvée"));
    }
    @DeleteMapping("/{matricule}")
    public ResponseEntity<Void> deleteVoiture(@PathVariable String
                                                      matricule) {
        voitureRepository.deleteById(matricule);
        return ResponseEntity.noContent().build();
    }
}