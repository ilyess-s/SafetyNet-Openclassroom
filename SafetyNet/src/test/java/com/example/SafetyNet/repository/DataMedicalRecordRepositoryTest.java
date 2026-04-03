package com.example.SafetyNet.repository;

import com.example.SafetyNet.model.MedicalRecords;
import com.example.SafetyNet.repository.Impl.DataMedicalRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DataMedicalRecordRepositoryTest {

    private DataMedicalRecordRepository medicalRecordRepository;

    @BeforeEach
    public void setUp() {
        medicalRecordRepository = new DataMedicalRecordRepository();

        // Préparation d'un dossier initial
        List<String> medications = new ArrayList<>(List.of("azithromycin:350mg"));
        List<String> allergies = new ArrayList<>(List.of("nillacilan"));
        MedicalRecords record = new MedicalRecords("John", "Boyd", "03/06/1984", medications, allergies);

        // On utilise save() pour remplir le repository de test
        medicalRecordRepository.save(record);
    }

    @Test
    public void testFindAll() {
        List<MedicalRecords> result = medicalRecordRepository.findAllRecords();
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }

    @Test
    public void testFindByFirstAndLastName_Success() {
        MedicalRecords result = medicalRecordRepository.findByFirstAndLastName("John", "Boyd");
        assertNotNull(result);
        assertEquals("03/06/1984", result.getBirthdate());
    }

    @Test
    public void testFindByFirstAndLastName_NotFound() {
        MedicalRecords result = medicalRecordRepository.findByFirstAndLastName("Unknown", "User");
        assertNull(result);
    }

    @Test
    public void testUpdate_Success() {
        // GIVEN: On prépare une version modifiée (nouveaux médicaments)
        List<String> newMedications = List.of("terramycin:10mg");
        MedicalRecords updatedRecord = new MedicalRecords("John", "Boyd", "03/06/1984", newMedications, List.of("nillacilan"));

        // WHEN
        boolean isUpdated = medicalRecordRepository.update(updatedRecord);

        // THEN
        assertTrue(isUpdated);
        MedicalRecords result = medicalRecordRepository.findByFirstAndLastName("John", "Boyd");
        assertEquals(1, result.getMedications().size());
        assertEquals("terramycin:10mg", result.getMedications().get(0));
    }

    @Test
    public void testUpdate_NotFound() {
        MedicalRecords nonExistent = new MedicalRecords("Ghost", "User", "01/01/2000", new ArrayList<>(), new ArrayList<>());

        boolean isUpdated = medicalRecordRepository.update(nonExistent);

        assertFalse(isUpdated);
    }

    @Test
    public void testDelete_Success() {
        // WHEN
        boolean isDeleted = medicalRecordRepository.delete("John", "Boyd");

        // THEN
        assertTrue(isDeleted);
        assertEquals(0, medicalRecordRepository.findAllRecords().size());
    }

    @Test
    public void testDelete_NotFound() {
        // WHEN
        boolean isDeleted = medicalRecordRepository.delete("Unknown", "User");

        // THEN
        assertFalse(isDeleted);
        assertEquals(1, medicalRecordRepository.findAllRecords().size());
    }
}