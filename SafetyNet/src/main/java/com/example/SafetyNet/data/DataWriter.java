package com.example.SafetyNet.data;

import com.example.SafetyNet.repository.FirestationRepository;
import com.example.SafetyNet.repository.MedicalRecordRepository;
import com.example.SafetyNet.repository.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class DataWriter {

    private final PersonRepository personRepository;
    private final FirestationRepository firestationRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final ObjectMapper objectMapper;

    // Chemin vers le fichier (à adapter selon votre environnement)
    private static final String FILE_PATH = "src/main/resources/data.json";

    public DataWriter(PersonRepository personRepository,
                      FirestationRepository firestationRepository,
                      MedicalRecordRepository medicalRecordRepository) {
        this.personRepository = personRepository;
        this.firestationRepository = firestationRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void saveAllData() {
        try {
            // 1. On prépare le wrapper avec l'intégralité des données en mémoire
            DataWrapper currentData = new DataWrapper();
            currentData.setPersons(personRepository.findAllPersons());
            currentData.setFirestations(firestationRepository.findAll());
            currentData.setMedicalrecords(medicalRecordRepository.findAllRecords());

            // 2. On écrase le fichier avec l'état complet (Ancien + Nouveau)
            objectMapper.writeValue(new File(FILE_PATH), currentData);

            System.out.println("Fichier data.json mis à jour avec succès.");
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture du fichier : " + e.getMessage());
            throw new RuntimeException("Échec de la sauvegarde des données");
        }
    }
}