package com.example.SafetyNet.service;

import com.example.SafetyNet.model.MedicalRecords;
import com.example.SafetyNet.repository.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public List<MedicalRecords> getAllRecords() {
        return medicalRecordRepository.findAllRecords();
    }

    public void addRecord(MedicalRecords record) {
        medicalRecordRepository.save(record);
    }

    public boolean updateRecord(MedicalRecords record) {
        return medicalRecordRepository.update(record);
    }

    public boolean deleteRecord(String firstName, String lastName) {
        return medicalRecordRepository.delete(firstName, lastName);
    }
}