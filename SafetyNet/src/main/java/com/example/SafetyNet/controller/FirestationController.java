package com.example.SafetyNet.controller;

import com.example.SafetyNet.model.Firestations;
import com.example.SafetyNet.service.FirestationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/firestation")
public class FirestationController {

    //injection du service
    private final FirestationService firestationService;

    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    @GetMapping
    public ResponseEntity<?> getPersonsByStation(@RequestParam(required = false) Integer stationNumber) {
        if (stationNumber == null) {
            // Si aucun numéro fourni (ex: GET /firestation), on renvoie tout
            throw new RuntimeException("Station not found");
        }

        // Si un numéro est fourni (ex: GET /firestation?stationNumber=3)
        Map<String, Object> response = firestationService.getPersonsByStation(stationNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/childAlert")
    public ResponseEntity<?> getChildrenByAddress(@RequestParam String address) {

        List<Map<String, Object>> children = firestationService.getChildrenByAddress(address);

        if (children.isEmpty()) {
            return ResponseEntity.ok(""); // chaîne vide si aucun enfant
        }

        return ResponseEntity.ok(children);
    }

    @GetMapping("/phoneAlert")
    public List<String> getPhoneNumbers(@RequestParam int firestation) {
        return firestationService.getPhoneNumbersByStation(firestation);
    }

    @GetMapping("/fire")
    public ResponseEntity<?> getFire(@RequestParam String address) {
        return ResponseEntity.ok(firestationService.getFireByAddress(address));
    }

    @GetMapping("/flood/stations")
    public ResponseEntity<?> getFlood(@RequestParam List<Integer> stations) {
        return ResponseEntity.ok(firestationService.getFloodByStations(stations));
    }

    //ajouter une station
    @PostMapping
    public ResponseEntity<String> addFirestation(@RequestBody Firestations firestation) {
        firestationService.addFirestation(firestation);
        return ResponseEntity.ok("Firestation added");
    }

    //modifier une station
    @PutMapping
    public ResponseEntity<String> updateFirestation(@RequestBody Firestations firestation) {
        boolean updated = firestationService.updateFirestation(firestation);
        if (updated) {
            return ResponseEntity.ok("Firestation updated");
        } else {
            return ResponseEntity.notFound().build();
        }    }

    //suprimer une station
    @DeleteMapping
    public ResponseEntity<String> deleteFirestation(@RequestParam String address) {
        boolean deleted = firestationService.deleteFirestation(address);
        if (deleted) {
            return ResponseEntity.ok("Firestation deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}


//ajouter un logger pour requettes entrantes et sortantes
//modifier le fichier json a chaque modification aved POST ou PUT (ecriture dans le fichier)