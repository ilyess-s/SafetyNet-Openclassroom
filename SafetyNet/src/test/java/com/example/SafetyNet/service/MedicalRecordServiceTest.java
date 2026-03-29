package com.example.SafetyNet.service;

import com.example.SafetyNet.data.DataWriter;
import com.example.SafetyNet.model.MedicalRecords;
import com.example.SafetyNet.repository.MedicalRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordServiceTest {

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private DataWriter dataWriter;

    @InjectMocks
    private MedicalRecordService medicalRecordService;

    private MedicalRecords record1;

    @BeforeEach
    public void setUp() {
        // Initialisation d'un dossier de test
        List<String> medications = List.of("azithromycin:350mg", "hydranencephaly:250mg");
        List<String> allergies = List.of("nillacilan");
        record1 = new MedicalRecords("John", "Boyd", "03/06/1984", medications, allergies);
    }

    @Test
    public void testGetAllMedicalRecords() {
        // GIVEN
        List<MedicalRecords> list = List.of(record1);
        when(medicalRecordRepository.findAllRecords()).thenReturn(list);

        // WHEN
        List<MedicalRecords> result = medicalRecordService.getAllRecords();

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(medicalRecordRepository, times(1)).findAllRecords();
    }

    @Test
    public void testAddMedicalRecord() {
        // WHEN
        medicalRecordService.addRecord(record1);

        // THEN
        verify(medicalRecordRepository, times(1)).save(record1);
    }

    @Test
    public void testUpdateMedicalRecord_Success() {
        // GIVEN
        when(medicalRecordRepository.update(any(MedicalRecords.class))).thenReturn(true);

        // WHEN
        boolean isUpdated = medicalRecordService.updateRecord(record1);

        // THEN
        assertTrue(isUpdated);
        verify(medicalRecordRepository, times(1)).update(record1);
    }

    @Test
    public void testUpdateMedicalRecord_Fail() {
        // GIVEN
        when(medicalRecordRepository.update(any(MedicalRecords.class))).thenReturn(false);

        // WHEN
        boolean isUpdated = medicalRecordService.updateRecord(record1);

        // THEN
        assertFalse(isUpdated);
    }

    @Test
    public void testDeleteMedicalRecord_Success() {
        // GIVEN
        String firstName = "John";
        String lastName = "Boyd";
        when(medicalRecordRepository.delete(firstName, lastName)).thenReturn(true);

        // WHEN
        boolean isDeleted = medicalRecordService.deleteRecord(firstName, lastName);

        // THEN
        assertTrue(isDeleted);
        verify(medicalRecordRepository, times(1)).delete(firstName, lastName);
    }

    @Test
    public void testDeleteMedicalRecord_Fail() {
        // GIVEN
        when(medicalRecordRepository.delete("Unknown", "User")).thenReturn(false);

        // WHEN
        boolean isDeleted = medicalRecordService.deleteRecord("Unknown", "User");

        // THEN
        assertFalse(isDeleted);
    }
}