package com.example.SafetyNet.controller;

import com.example.SafetyNet.model.MedicalRecords;
import com.example.SafetyNet.service.MedicalRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping
    public List<MedicalRecords> getAll() {
        return medicalRecordService.getAllRecords();
    }

    @PostMapping
    public ResponseEntity<String> add(@RequestBody MedicalRecords record) {
        medicalRecordService.addRecord(record);
        return ResponseEntity.ok("Medical record added");
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody MedicalRecords record) {
        boolean updated = medicalRecordService.updateRecord(record);
        if (updated) {
            return ResponseEntity.ok("Medical record updated");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam String firstName, @RequestParam String lastName) {
        boolean deleted = medicalRecordService.deleteRecord(firstName, lastName);
        if (deleted) {
            return ResponseEntity.ok("Medical record deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}