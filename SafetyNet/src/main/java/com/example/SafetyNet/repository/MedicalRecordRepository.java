package com.example.SafetyNet.repository;

import com.example.SafetyNet.model.MedicalRecords;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordRepository {
    List<MedicalRecords> findAllRecords();
    void save(MedicalRecords record);
    boolean update(MedicalRecords record);
    boolean delete(String firstName, String lastName);
    default MedicalRecords findByFirstAndLastName(String firstName, String lastName) {
        return findAllRecords().stream()
                .filter(r -> r.getFirstName().equals(firstName) && r.getLastName().equals(lastName))
                .findFirst()
                .orElse(null);
    }
}