package com.example.SafetyNet.service;

import com.example.SafetyNet.model.MedicalRecords;
import com.example.SafetyNet.model.Person;
import com.example.SafetyNet.repository.MedicalRecordRepository;
import com.example.SafetyNet.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public PersonService(PersonRepository personRepository, MedicalRecordRepository medicalRecordRepository) {
        this.personRepository = personRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public List<Person> getAllPersons() {
        return personRepository.findAllPersons();
    }

    public void addPerson(Person person) {
        personRepository.save(person);
    }

    public boolean updatePerson(Person person) {
        return personRepository.update(person);
    }

    public boolean deletePerson(String firstName, String lastName) {
        return personRepository.delete(firstName, lastName);
    }

    private int calculateAge(String birthdate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birth = LocalDate.parse(birthdate, formatter);
        return Period.between(birth, LocalDate.now()).getYears();
    }

    public List<Map<String, Object>> getPersonInfo(String lastName) {

        List<Person> persons = personRepository.findAllPersons().stream()
                .filter(p -> p.getLastName().equalsIgnoreCase(lastName))
                .toList();

        List<Map<String, Object>> result = new ArrayList<>();

        for (Person person : persons) {

            MedicalRecords record = medicalRecordRepository
                    .findByFirstAndLastName(person.getFirstName(), person.getLastName());

            int age = 0;
            List<String> medications = new ArrayList<>();
            List<String> allergies = new ArrayList<>();

            if (record != null) {
                age = calculateAge(record.getBirthdate());
                medications = record.getMedications();
                allergies = record.getAllergies();
            }

            Map<String, Object> info = new HashMap<>();
            info.put("firstName", person.getFirstName());
            info.put("lastName", person.getLastName());
            info.put("address", person.getAddress());
            info.put("age", age);
            info.put("email", person.getEmail());
            info.put("medications", medications);
            info.put("allergies", allergies);

            result.add(info);
        }
        return result;
    }
    public List<String> getCommunityEmails(String city) {

        return personRepository.findAllPersons().stream()
                .filter(p -> p.getCity().equalsIgnoreCase(city))
                .map(Person::getEmail)
                .distinct() // évite les doublons
                .toList();
    }
}