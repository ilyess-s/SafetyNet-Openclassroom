package com.example.SafetyNet.data;

import com.example.SafetyNet.repository.FirestationRepository;
import com.example.SafetyNet.repository.MedicalRecordRepository;
import com.example.SafetyNet.repository.PersonRepository;
import com.example.SafetyNet.service.FirestationService;
import com.example.SafetyNet.service.MedicalRecordService;
import com.example.SafetyNet.service.PersonService;
import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.io.PrintStream;

@Component
public class DataLoader {
    //injection du repository
    private final PersonRepository personRepository;
    private final FirestationRepository firestationRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public DataLoader(PersonRepository personRepository, FirestationRepository firestationRepository, MedicalRecordRepository medicalRecordRepository) {

        this.personRepository = personRepository;
        this.firestationRepository = firestationRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }
    //chargement des données avant le debut des responses aux requetes
    @PostConstruct
    public void loadData() throws Exception {

        ObjectMapper mapper = new ObjectMapper(); //Utilisé pour convertir un objet JSON en objet JAVA

        InputStream input = getClass().getResourceAsStream("/data.json"); //Lis le fichier Data.json (getResourceAsStream est utilisé pour donner le fichier a jackson en stream pour qu'il puisse le lire)
        if (input == null) throw new Exception("Data.json not found");

        //conversion json -> datawrapper qui contien persons, firestations et medical records
        DataWrapper data = mapper.readValue(input, DataWrapper.class);

        //injection via les services (pas directement dans un repository)
        data.getPersons().forEach(personRepository::save);
        data.getFirestations().forEach(firestationRepository::save);
        data.getMedicalrecords().forEach(medicalRecordRepository::save);
    }
}
