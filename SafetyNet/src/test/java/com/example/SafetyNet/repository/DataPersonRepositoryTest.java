package com.example.SafetyNet.repository;

import com.example.SafetyNet.model.Person;
import com.example.SafetyNet.repository.Impl.DataPersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DataPersonRepositoryTest {

    private DataPersonRepository personRepository;

    @BeforeEach
    public void setUp() {
        personRepository = new DataPersonRepository();

        // Initialisation avec une liste de test propre
        List<Person> initialPersons = new ArrayList<>();
        initialPersons.add(new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"));
        initialPersons.add(new Person("Jacob", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513", "drk@email.com"));

        personRepository.setPersons(initialPersons);
    }

    @Test
    public void testFindAll() {
        List<Person> result = personRepository.findAllPersons();
        assertEquals(2, result.size());
    }

    @Test
    public void testSavePerson() {
        Person newPerson = new Person("Jane", "Doe", "Address", "City", "Zip", "Phone", "Email");
        personRepository.save(newPerson);

        assertEquals(3, personRepository.findAllPersons().size());
        assertTrue(personRepository.findAllPersons().contains(newPerson));
    }

    @Test
    public void testUpdatePerson_Success() {
        // GIVEN: On crée une version modifiée de John Boyd
        Person updatedJohn = new Person("John", "Boyd", "New Address", "Culver", "97451", "000-000-0000", "new@email.com");

        // WHEN
        boolean isUpdated = personRepository.update(updatedJohn);

        // THEN
        assertTrue(isUpdated);
        Person result = personRepository.findAllPersons().stream()
                .filter(p -> p.getFirstName().equals("John") && p.getLastName().equals("Boyd"))
                .findFirst()
                .orElse(null);

        assertNotNull(result);
        assertEquals("New Address", result.getAddress());
        assertEquals("000-000-0000", result.getPhone());
    }

    @Test
    public void testUpdatePerson_NotFound() {
        // GIVEN: Une personne qui n'existe pas dans la liste initiale
        Person nonExistent = new Person("Ghost", "User", "Addr", "City", "Zip", "Phone", "Email");

        // WHEN
        boolean isUpdated = personRepository.update(nonExistent);

        // THEN
        assertFalse(isUpdated);
    }

    @Test
    public void testDeletePerson_Success() {
        // WHEN
        boolean isDeleted = personRepository.delete("John", "Boyd");

        // THEN
        assertTrue(isDeleted);
        assertEquals(1, personRepository.findAllPersons().size());
        // On vérifie que seul Jacob Boyd reste
        assertEquals("Jacob", personRepository.findAllPersons().get(0).getFirstName());
    }

    @Test
    public void testDeletePerson_NotFound() {
        // WHEN
        boolean isDeleted = personRepository.delete("Unknown", "User");

        // THEN
        assertFalse(isDeleted);
        assertEquals(2, personRepository.findAllPersons().size()); // La taille n'a pas bougé
    }
}