package com.example.SafetyNet.repository;

import com.example.SafetyNet.model.Firestations;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FirestationRepository {
    List<Firestations> findAll();
    void save(Firestations firestation);
    boolean update(Firestations firestation);
    boolean delete(String address);
}