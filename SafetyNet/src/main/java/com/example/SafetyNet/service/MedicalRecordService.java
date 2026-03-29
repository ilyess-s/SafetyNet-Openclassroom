package com.example.SafetyNet.service;

import com.example.SafetyNet.data.DataWriter;
import com.example.SafetyNet.model.MedicalRecords;
import com.example.SafetyNet.repository.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final DataWriter dataWriter;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository, DataWriter dataWriter) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.dataWriter = dataWriter;
    }

    public List<MedicalRecords> getAllRecords() {
        return medicalRecordRepository.findAllRecords();
    }

    public void addRecord(MedicalRecords record) {
        medicalRecordRepository.save(record);
        dataWriter.saveAllData();
    }

    public boolean updateRecord(MedicalRecords record) {
        boolean isUpdated = medicalRecordRepository.update(record);
        if (isUpdated) {
            dataWriter.saveAllData(); // Sauvegarde après modification
        }
        return isUpdated;
    }

    public boolean deleteRecord(String firstName, String lastName) {
        boolean isDeleted = medicalRecordRepository.delete(firstName, lastName);
        if (isDeleted) {
            dataWriter.saveAllData(); // Sauvegarde après suppression
        }
        return isDeleted;
    }
}