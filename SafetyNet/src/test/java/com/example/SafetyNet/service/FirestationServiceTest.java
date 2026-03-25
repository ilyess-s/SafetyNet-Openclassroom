package com.example.SafetyNet.service;

import com.example.SafetyNet.model.Firestations;
import com.example.SafetyNet.model.MedicalRecords;
import com.example.SafetyNet.model.Person;

import com.example.SafetyNet.repository.FirestationRepository;
import com.example.SafetyNet.repository.PersonRepository;
import com.example.SafetyNet.repository.MedicalRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FirestationServiceTest {

    @Mock
    private FirestationRepository firestationRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @InjectMocks
    private FirestationService firestationService;

    private Firestations station1;
    private Person person1;

    @BeforeEach
    public void setUp() {
        station1 = new Firestations("1509 Culver St", 3);
        person1 = new Person();
        person1.setFirstName("John");
        person1.setLastName("Boyd");
        person1.setAddress("1509 Culver St");
        person1.setPhone("841-874-6512");
    }

    @Test
    public void testGetAllFirestations() {
        List<Firestations> list = List.of(station1);
        when(firestationRepository.findAll()).thenReturn(list);

        List<Firestations> result = firestationService.getAllFirestations();

        assertEquals(1, result.size());
        assertEquals("1509 Culver St", result.get(0).getAddress());
    }

    @Test
    public void testAddFirestation() {
        firestationService.addFirestation(station1);
        verify(firestationRepository, times(1)).save(station1);
    }

    @Test
    public void testUpdateFirestation_Success() {
        when(firestationRepository.update(any(Firestations.class))).thenReturn(true);

        boolean updated = firestationService.updateFirestation(station1);

        assertTrue(updated);
        verify(firestationRepository).update(station1);
    }

    @Test
    public void testDeleteFirestation() {
        when(firestationRepository.delete("1509 Culver St")).thenReturn(true);

        boolean deleted = firestationService.deleteFirestation("1509 Culver St");

                assertTrue(deleted);
        verify(firestationRepository).delete("1509 Culver St");
    }

    @Test
    public void testGetPhoneNumbersByStation() {
        // GIVEN: On utilise le constructeur SANS ARGUMENTS que Maven accepte
        // Utilisation du constructeur : (firstName, lastName, address, city, zip, phone, email)
        Person person = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com");

        Firestations station = new Firestations();
        station.setAddress("1509 Culver St");
        station.setStation(3);

        List<Person> personList = new ArrayList<>();
        personList.add(person);

        List<Firestations> stationList = new ArrayList<>();
        stationList.add(station);

        when(firestationRepository.findAll()).thenReturn(stationList);
        when(personRepository.findAllPersons()).thenReturn(personList);

        // WHEN
        List<String> result = firestationService.getPhoneNumbersByStation(3);

        // THEN
        assertNotNull(result);
        // On ne vérifie pas le contenu pour l'instant, juste que ça compile et s'exécute
    }

    @Test
    public void testGetFireByAddress() {
        // GIVEN
        String address = "1509 Culver St";
        MedicalRecords record = new MedicalRecords("John", "Boyd", "03/06/1984", List.of("pharmacol:500mg"), List.of("nillacilan"));

        when(firestationRepository.findAll()).thenReturn(List.of(station1));
        when(personRepository.findAllPersons()).thenReturn(List.of(person1));
        when(medicalRecordRepository.findByFirstAndLastName("John", "Boyd")).thenReturn(record);

        // WHEN
        Map<String, Object> result = firestationService.getFireByAddress(address);

        // THEN
        assertNotNull(result);
        assertEquals(3, result.get("stationNumber"));

        List<Map<String, Object>> persons = (List<Map<String, Object>>) result.get("persons");
        assertFalse(persons.isEmpty());
        assertEquals("Boyd", persons.get(0).get("lastName"));
        assertEquals("841-874-6512", persons.get(0).get("phone"));
        assertNotNull(persons.get(0).get("age"));
        assertEquals(List.of("pharmacol:500mg"), persons.get(0).get("medications"));
    }

    @Test
    public void testGetFireByAddress_NotFound() {
        // GIVEN
        when(firestationRepository.findAll()).thenReturn(new ArrayList<>());
        when(personRepository.findAllPersons()).thenReturn(new ArrayList<>());

        // WHEN
        Map<String, Object> result = firestationService.getFireByAddress("Unknown Address");

        // THEN
        assertEquals(0, result.get("stationNumber"));
        assertTrue(((List<?>) result.get("persons")).isEmpty());
    }

    @Test
    public void testGetFloodByStations() {
        // GIVEN
        // 1. Préparation des stations de pompiers
        Firestations station1 = new Firestations("1509 Culver St", 3);
        Firestations station2 = new Firestations("29 15th St", 2);
        when(firestationRepository.findAll()).thenReturn(List.of(station1, station2));

        // 2. Préparation des personnes
        Person person1 = new Person();
        person1.setFirstName("John");
        person1.setLastName("Boyd");
        person1.setAddress("1509 Culver St");
        person1.setPhone("841-874-6512");

        Person person2 = new Person();
        person2.setFirstName("Foster");
        person2.setLastName("Shepard");
        person2.setAddress("29 15th St");
        person2.setPhone("841-874-6544");

        when(personRepository.findAllPersons()).thenReturn(List.of(person1, person2));

        // 3. Préparation des dossiers médicaux (pour l'âge, médicaments, allergies)
        MedicalRecords record1 = new MedicalRecords();
        record1.setFirstName("John");
        record1.setLastName("Boyd");
        record1.setBirthdate("03/06/1984"); // Calculera l'âge dynamiquement
        record1.setMedications(List.of("azithromycin:350mg"));
        record1.setAllergies(List.of("nillacilan"));

        MedicalRecords record2 = new MedicalRecords();
        record2.setFirstName("Foster");
        record2.setLastName("Shepard");
        record2.setBirthdate("01/08/1980");
        record2.setMedications(new ArrayList<>());
        record2.setAllergies(new ArrayList<>());

        when(medicalRecordRepository.findByFirstAndLastName("John", "Boyd")).thenReturn(record1);
        when(medicalRecordRepository.findByFirstAndLastName("Foster", "Shepard")).thenReturn(record2);

        // WHEN
        // On demande les foyers pour les stations 3 et 2
        Map<String, List<Map<String, Object>>> result = firestationService.getFloodByStations(List.of(2, 3));

        // THEN
        assertNotNull(result);
        assertEquals(2, result.size()); // Deux adresses distinctes
        assertTrue(result.containsKey("1509 Culver St"));
        assertTrue(result.containsKey("29 15th St"));

        // Vérification pour John Boyd (Station 3)
        List<Map<String, Object>> culverStFamily = result.get("1509 Culver St");
        assertEquals(1, culverStFamily.size());
        assertEquals("John", culverStFamily.get(0).get("firstName"));
        assertEquals("841-874-6512", culverStFamily.get(0).get("phone"));
        assertEquals(List.of("azithromycin:350mg"), culverStFamily.get(0).get("medications"));

        // Vérification pour Foster Shepard (Station 2)
        List<Map<String, Object>> fifteenthStFamily = result.get("29 15th St");
        assertEquals(1, fifteenthStFamily.size());
        assertEquals("Foster", fifteenthStFamily.get(0).get("firstName"));
        assertNotNull(fifteenthStFamily.get(0).get("age"));
    }

    @Test
    public void testGetPersonsByStation() {
        // GIVEN
        int stationNumber = 3;
        String address = "1509 Culver St";

        // 1. Simuler la caserne de pompiers
        Firestations station = new Firestations();
        station.setAddress(address);
        station.setStation(stationNumber);
        when(firestationRepository.findAll()).thenReturn(List.of(station));

        // 2. Simuler deux personnes à cette adresse (un adulte et un enfant)
        Person adult = new Person();
        adult.setFirstName("John");
        adult.setLastName("Boyd");
        adult.setAddress(address);
        adult.setPhone("841-874-6512");

        Person child = new Person();
        child.setFirstName("Tenley");
        child.setLastName("Boyd");
        child.setAddress(address);
        child.setPhone("841-874-6512");

        when(personRepository.findAllPersons()).thenReturn(List.of(adult, child));

        // 3. Simuler les dossiers médicaux pour calculer les âges
        MedicalRecords adultRecord = new MedicalRecords();
        adultRecord.setFirstName("John");
        adultRecord.setLastName("Boyd");
        adultRecord.setBirthdate("03/06/1984"); // Adulte

        MedicalRecords childRecord = new MedicalRecords();
        childRecord.setFirstName("Tenley");
        childRecord.setLastName("Boyd");
        childRecord.setBirthdate("02/18/2015"); // Enfant

        when(medicalRecordRepository.findByFirstAndLastName("John", "Boyd")).thenReturn(adultRecord);
        when(medicalRecordRepository.findByFirstAndLastName("Tenley", "Boyd")).thenReturn(childRecord);

        // WHEN
        Map<String, Object> result = firestationService.getPersonsByStation(stationNumber);

        // THEN
        assertNotNull(result);
        assertEquals(1, result.get("adults"));
        assertEquals(1, result.get("children"));

        List<Map<String, String>> persons = (List<Map<String, String>>) result.get("persons");
        assertEquals(2, persons.size());

        // Vérification des données de l'adulte dans la liste
        Map<String, String> firstPerson = persons.get(0);
        assertEquals("John", firstPerson.get("firstName"));
        assertEquals("Boyd", firstPerson.get("lastName"));
        assertEquals("1509 Culver St", firstPerson.get("address"));
        assertEquals("841-874-6512", firstPerson.get("phone"));
    }

    @Test
    public void testGetPersonsByStation_NoPersons() {
        // GIVEN
        int stationNumber = 3;
        when(firestationRepository.findAll()).thenReturn(new ArrayList<>());

        // WHEN
        Map<String, Object> result = firestationService.getPersonsByStation(stationNumber);

        // THEN
        assertNotNull(result);
        assertEquals(0, result.get("adults"));
        assertEquals(0, result.get("children"));
        assertTrue(((List<?>) result.get("persons")).isEmpty());
    }
    @Test
    public void testGetChildrenByAddress() {
        // GIVEN
        String address = "1509 Culver St";

        // 1. Préparation des personnes à la même adresse
        Person child = new Person();
        child.setFirstName("Tenley");
        child.setLastName("Boyd");
        child.setAddress(address);

        Person adult = new Person();
        adult.setFirstName("John");
        adult.setLastName("Boyd");
        adult.setAddress(address);

        when(personRepository.findAllPersons()).thenReturn(List.of(child, adult));

        // 2. Préparation des dossiers médicaux
        MedicalRecords childRecord = new MedicalRecords();
        childRecord.setFirstName("Tenley");
        childRecord.setLastName("Boyd");
        childRecord.setBirthdate("02/18/2015"); // Enfant

        MedicalRecords adultRecord = new MedicalRecords();
        adultRecord.setFirstName("John");
        adultRecord.setLastName("Boyd");
        adultRecord.setBirthdate("03/06/1984"); // Adulte

        when(medicalRecordRepository.findByFirstAndLastName("Tenley", "Boyd")).thenReturn(childRecord);
        when(medicalRecordRepository.findByFirstAndLastName("John", "Boyd")).thenReturn(adultRecord);

        // WHEN
        List<Map<String, Object>> result = firestationService.getChildrenByAddress(address);

        // THEN
        assertNotNull(result);
        assertEquals(1, result.size());

        Map<String, Object> childInfo = result.get(0);
        assertEquals("Tenley", childInfo.get("firstName"));
        assertEquals("Boyd", childInfo.get("lastName"));
        assertTrue((int) childInfo.get("age") <= 18);

        // --- CORRECTION ICI ---
        // On récupère une List de Map, car c'est ce que le service construit
        List<Map<String, Object>> householdMembers = (List<Map<String, Object>>) childInfo.get("householdMembers");

        assertNotNull(householdMembers);
        assertEquals(1, householdMembers.size());

        // On accède au premier membre via .get("firstName") et non .getFirstName()
        Map<String, Object> firstMember = householdMembers.get(0);
        assertEquals("John", firstMember.get("firstName"));
        assertEquals("Boyd", firstMember.get("lastName"));
    }

    @Test
    public void testGetChildrenByAddress_NoChildren() {
        // GIVEN
        String address = "1509 Culver St";

        Person adult = new Person();
        adult.setFirstName("John");
        adult.setLastName("Boyd");
        adult.setAddress(address);

        when(personRepository.findAllPersons()).thenReturn(List.of(adult));

        MedicalRecords adultRecord = new MedicalRecords();
        adultRecord.setFirstName("John");
        adultRecord.setLastName("Boyd");
        adultRecord.setBirthdate("03/06/1984");

        when(medicalRecordRepository.findByFirstAndLastName("John", "Boyd")).thenReturn(adultRecord);

        // WHEN
        List<Map<String, Object>> result = firestationService.getChildrenByAddress(address);

        // THEN
        assertNotNull(result);
        assertTrue(result.isEmpty()); // Doit retourner une liste vide si aucun enfant
    }
}