package com.example.SafetyNet.repository;

import com.example.SafetyNet.model.Firestations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DataFirestationRepositoryTest {

    private DataFirestationRepository firestationRepository;

    @BeforeEach
    public void setUp() {
        firestationRepository = new DataFirestationRepository();

        // Ajout d'une caserne de test initiale
        Firestations station1 = new Firestations("1509 Culver St", 3);
        firestationRepository.save(station1);
    }

    @Test
    public void testFindAll() {
        List<Firestations> result = firestationRepository.findAll();
        assertEquals(1, result.size());
        assertEquals("1509 Culver St", result.get(0).getAddress());
    }

    @Test
    public void testSave_Success() {
        Firestations newStation = new Firestations("29 15th St", 2);
        firestationRepository.save(newStation);

        assertEquals(2, firestationRepository.findAll().size());
        assertTrue(firestationRepository.findAll().stream()
                .anyMatch(f -> f.getAddress().equals("29 15th St")));
    }

    @Test
    public void testUpdate_Success() {
        // GIVEN: On change le numéro de station pour une adresse existante
        Firestations updatedStation = new Firestations("1509 Culver St", 4);

        // WHEN
        boolean isUpdated = firestationRepository.update(updatedStation);

        // THEN
        assertTrue(isUpdated);
        assertEquals(4, firestationRepository.findAll().get(0).getStation());
    }

    @Test
    public void testUpdate_NotFound() {
        // GIVEN: Une adresse qui n'existe pas
        Firestations unknownStation = new Firestations("Unknown Address", 1);

        // WHEN
        boolean isUpdated = firestationRepository.update(unknownStation);

        // THEN
        assertFalse(isUpdated);
    }

    @Test
    public void testDelete_Success() {
        // WHEN
        boolean isDeleted = firestationRepository.delete("1509 Culver St");

        // THEN
        assertTrue(isDeleted);
        assertEquals(0, firestationRepository.findAll().size());
    }

    @Test
    public void testDelete_NotFound() {
        // WHEN
        boolean isDeleted = firestationRepository.delete("Non Existent Address");

        // THEN
        assertFalse(isDeleted);
        assertEquals(1, firestationRepository.findAll().size());
    }
}