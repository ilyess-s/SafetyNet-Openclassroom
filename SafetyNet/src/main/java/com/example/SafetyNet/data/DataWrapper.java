package com.example.SafetyNet.data;

import com.example.SafetyNet.model.Firestations;
import com.example.SafetyNet.model.MedicalRecords;
import com.example.SafetyNet.model.Person;

import java.util.List;

public class DataWrapper {
    private List<Person> persons;
    private List<Firestations> firestations;
    private List<MedicalRecords> medicalrecords;


    //getters
    public List<Person> getPersons() {
        return persons;
    }

    public List<Firestations> getFirestations() {
        return firestations;
    }

    public List<MedicalRecords> getMedicalrecords() {
        return medicalrecords;
    }

    //setters
    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public void setFirestations(List<Firestations> firestations) {
        this.firestations = firestations;
    }

    public void setMedicalrecords(List<MedicalRecords> medicalrecords) {
        this.medicalrecords = medicalrecords;
    }
}
