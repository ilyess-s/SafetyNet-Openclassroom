package com.example.SafetyNet.service;

import com.example.SafetyNet.data.DataWriter;
import com.example.SafetyNet.model.MedicalRecords;
import com.example.SafetyNet.model.Person;
import com.example.SafetyNet.repository.MedicalRecordRepository;
import com.example.SafetyNet.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private DataWriter dataWriter;

    @InjectMocks
    private PersonService personService;

    @Test
    public void testGetPersonInfo() {
        // GIVEN
        String lastName = "Boyd";

        // 1. Simuler une personne trouvée
        Person person = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");
        when(personRepository.findAllPersons()).thenReturn(List.of(person));

        // 2. Simuler son dossier médical
        MedicalRecords record = new MedicalRecords();
        record.setFirstName("John");
        record.setLastName("Boyd");
        record.setBirthdate("03/06/1984");
        record.setMedications(List.of("azithromycin:350mg"));
        record.setAllergies(List.of("nillacilan"));
        when(medicalRecordRepository.findByFirstAndLastName("John", "Boyd")).thenReturn(record);

        // WHEN
        List<Map<String, Object>> result = personService.getPersonInfo(lastName);

        // THEN
        assertNotNull(result);
        assertFalse(result.isEmpty());

        Map<String, Object> info = result.get(0);
        assertEquals("Boyd", info.get("lastName"));
        assertEquals("1509 Culver St", info.get("address"));
        assertEquals("jaboyd@email.com", info.get("email"));
        assertTrue((int) info.get("age") > 0);
        assertEquals(List.of("azithromycin:350mg"), info.get("medications"));
    }

    @Test
    public void testGetPersonInfo_NotFound() {
        // GIVEN
        String lastName = "Unknown";
        when(personRepository.findAllPersons()).thenReturn(List.of());

        // WHEN
        List<Map<String, Object>> result = personService.getPersonInfo(lastName);

        // THEN
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}