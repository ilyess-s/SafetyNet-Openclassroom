package com.example.SafetyNet.data;

import com.example.SafetyNet.service.FirestationService;
import com.example.SafetyNet.service.MedicalRecordService;
import com.example.SafetyNet.service.PersonService;
import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

@Component
public class DataLoader {
    //injection du repository
    private final PersonService personService;
    private final FirestationService firestationService;
    private final MedicalRecordService medicalRecordService;

    public DataLoader(PersonService personService, FirestationService firestationService, MedicalRecordService medicalRecordService) {
        this.personService = personService;
        this.firestationService = firestationService;
        this.medicalRecordService = medicalRecordService;
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
        data.getPersons().forEach(personService::addPerson);
        data.getFirestations().forEach(firestationService::addFirestation);
        data.getMedicalrecords().forEach(medicalRecordService::addRecord);
    }
}
