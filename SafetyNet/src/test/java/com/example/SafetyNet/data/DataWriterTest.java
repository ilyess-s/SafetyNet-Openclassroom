package com.example.SafetyNet.data;

import com.example.SafetyNet.model.Firestations;
import com.example.SafetyNet.model.MedicalRecords;
import com.example.SafetyNet.model.Person;
import com.example.SafetyNet.repository.FirestationRepository;
import com.example.SafetyNet.repository.MedicalRecordRepository;
import com.example.SafetyNet.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataWriterTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private FirestationRepository firestationRepository;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @InjectMocks
    private DataWriter dataWriter;

    @Test
    void saveAllData_ShouldCallAllRepositoriesAndWriteFile() {
        // GIVEN
        List<Person> persons = Collections.singletonList(new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com"));
        List<Firestations> stations = Collections.singletonList(new Firestations("1509 Culver St", 3));
        List<MedicalRecords> records = Collections.singletonList(new MedicalRecords("John", "Boyd", "03/06/1984", Collections.emptyList(), Collections.emptyList()));

        when(personRepository.findAllPersons()).thenReturn(persons);
        when(firestationRepository.findAll()).thenReturn(stations);
        when(medicalRecordRepository.findAllRecords()).thenReturn(records);

        // WHEN
        dataWriter.saveAllData();

        // THEN
        // On vérifie que le writer a bien demandé les données à chaque repository
        verify(personRepository, times(1)).findAllPersons();
        verify(firestationRepository, times(1)).findAll();
        verify(medicalRecordRepository, times(1)).findAllRecords();

        // On vérifie si le fichier a bien été créé/modifié sur le disque
        File file = new File("src/main/resources/data.json");
        assertTrue(file.exists(), "Le fichier data.json devrait exister après la sauvegarde");
    }
}