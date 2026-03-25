package com.example.SafetyNet.service;

import com.example.SafetyNet.model.Firestations;
import com.example.SafetyNet.model.MedicalRecords;
import com.example.SafetyNet.model.Person;
import com.example.SafetyNet.repository.FirestationRepository;
import com.example.SafetyNet.repository.MedicalRecordRepository;
import com.example.SafetyNet.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class FirestationService {

    private final FirestationRepository firestationRepository;
    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public FirestationService(FirestationRepository firestationRepository, PersonRepository personRepository, MedicalRecordRepository medicalRecordRepository) {
        this.firestationRepository = firestationRepository;
        this.personRepository = personRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public List<Firestations> getAllFirestations() {
        return firestationRepository.findAll();
    }

    public void addFirestation(Firestations firestation) {
        firestationRepository.save(firestation);
    }

    public boolean updateFirestation(Firestations firestation) {
        return firestationRepository.update(firestation);
    }

    public boolean deleteFirestation(String address) {
        return firestationRepository.delete(address);
    }
    // Méthode utilitaire pour calculer l'âge
    private int calculateAge(String birthdate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate birth = LocalDate.parse(birthdate, formatter);
        return Period.between(birth, LocalDate.now()).getYears();
    }

    // Liste des personnes couvertes par une station
    public Map<String, Object> getPersonsByStation(int stationNumber) {
        List<String> addresses = firestationRepository.findAll().stream()
                .filter(f -> f.getStation() == stationNumber)
                .map(Firestations::getAddress)
                .toList();

        List<Person> persons = personRepository.findAllPersons().stream()
                .filter(p -> addresses.contains(p.getAddress()))
                .toList();

        int adults = 0;
        int children = 0;

        List<Map<String, String>> personDetails = new ArrayList<>();

        for (Person person : persons) {
            MedicalRecords record = medicalRecordRepository.findByFirstAndLastName(person.getFirstName(), person.getLastName());
            int age = 0;
            if (record != null) {
                age = calculateAge(record.getBirthdate());
            }

            if (age <= 18) children++;
            else adults++;

            Map<String, String> info = new HashMap<>();
            info.put("firstName", person.getFirstName());
            info.put("lastName", person.getLastName());
            info.put("address", person.getAddress());
            info.put("phone", person.getPhone());
            personDetails.add(info);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("persons", personDetails);
        result.put("adults", adults);
        result.put("children", children);

        return result;
    }

    // Liste des enfants à une adresse
    public List<Map<String, Object>> getChildrenByAddress(String address) {
        List<Person> personsAtAddress = personRepository.findAllPersons().stream()
                .filter(p -> p.getAddress().equals(address))
                .toList();

        List<Map<String, Object>> children = new ArrayList<>();

        for (Person person : personsAtAddress) {

            MedicalRecords record = medicalRecordRepository
                    .findByFirstAndLastName(person.getFirstName(), person.getLastName());

            if (record == null) continue;

            int age = calculateAge(record.getBirthdate());

            if (age <= 18) {
                Map<String, Object> child = new HashMap<>();
                child.put("firstName", person.getFirstName());
                child.put("lastName", person.getLastName());
                child.put("age", age);

                // autres membres du foyer
                List<Map<String, String>> householdMembers = personsAtAddress.stream()
                        .filter(p -> !(p.getFirstName().equals(person.getFirstName())
                                && p.getLastName().equals(person.getLastName())))
                        .map(p -> Map.of(
                                "firstName", p.getFirstName(),
                                "lastName", p.getLastName()
                        ))
                        .toList();

                child.put("householdMembers", householdMembers);

                children.add(child);
            }
        }
        return children;
    }

    // Liste des numéros de téléphone des habitants desservis par une station
    public List<String> getPhoneNumbersByStation(int stationNumber) {
        List<String> addresses = firestationRepository.findAll().stream()
                .filter(f -> f.getStation() == stationNumber)
                .map(Firestations::getAddress)
                .toList();

        return personRepository.findAllPersons().stream()
                .filter(p -> addresses.contains(p.getAddress()))
                .map(Person::getPhone)
                .distinct()
                .toList();
    }
    public Map<String, Object> getFireByAddress(String address) {
        // 1. Trouver le numéro de station pour cette adresse
        int stationNumber = firestationRepository.findAll().stream()
                .filter(f -> f.getAddress().equalsIgnoreCase(address))
                .map(Firestations::getStation)
                .findFirst()
                .orElse(0); // Retourne 0 si non trouvé, ce qui permet de passer le test NotFound

        // 2. Trouver les personnes habitant à cette adresse
        List<Person> persons = personRepository.findAllPersons().stream()
                .filter(p -> p.getAddress().equalsIgnoreCase(address))
                .toList();

        List<Map<String, Object>> personsInfo = new ArrayList<>();
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
            info.put("lastName", person.getLastName());
            info.put("phone", person.getPhone());
            info.put("age", age);
            info.put("medications", medications);
            info.put("allergies", allergies);
            personsInfo.add(info);
        }

        // 3. Construire le résultat final
        Map<String, Object> result = new HashMap<>();
        result.put("stationNumber", stationNumber); // C'est cette ligne qui manquait probablement
        result.put("persons", personsInfo);

        return result;
    }

    public Map<String, List<Map<String, Object>>> getFloodByStations(List<Integer> stations) {

        //Trouver les adresses des stations demandées
        List<String> addresses = firestationRepository.findAll().stream()
                .filter(f -> stations.contains(f.getStation()))
                .map(Firestations::getAddress)
                .distinct()
                .toList();

        //Récupérer toutes les personnes concernées
        List<Person> persons = personRepository.findAllPersons().stream()
                .filter(p -> addresses.contains(p.getAddress()))
                .toList();

        //Regroupement par adresse
        Map<String, List<Map<String, Object>>> result = new HashMap<>();

        for (Person person : persons) {

            String address = person.getAddress();

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

            Map<String, Object> personInfo = new HashMap<>();
            personInfo.put("firstName", person.getFirstName());
            personInfo.put("lastName", person.getLastName());
            personInfo.put("phone", person.getPhone());
            personInfo.put("age", age);
            personInfo.put("medications", medications);
            personInfo.put("allergies", allergies);

            // Ajouter dans la bonne adresse
            result.computeIfAbsent(address, k -> new ArrayList<>()).add(personInfo);
        }

        return result;
    }
}