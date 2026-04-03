package com.example.SafetyNet.repository.Impl;

import com.example.SafetyNet.model.MedicalRecords;
import com.example.SafetyNet.repository.MedicalRecordRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DataMedicalRecordRepository implements MedicalRecordRepository {

    private List<MedicalRecords> records = new ArrayList<>();

    @Override
    public List<MedicalRecords> findAllRecords() {
        return records;
    }

    @Override
    public void save(MedicalRecords record) {
        records.add(record);
    }

    @Override
    public boolean update(MedicalRecords updatedRecord) {
        for (int i = 0; i < records.size(); i++) {
            MedicalRecords r = records.get(i);
            if (r.getFirstName().equals(updatedRecord.getFirstName()) &&
                    r.getLastName().equals(updatedRecord.getLastName())) {
                records.set(i, updatedRecord);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(String firstName, String lastName) {
        return records.removeIf(r -> r.getFirstName().equals(firstName) && r.getLastName().equals(lastName));
    }
}