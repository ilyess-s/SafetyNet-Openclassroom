package com.example.SafetyNet.repository;

import com.example.SafetyNet.model.Person;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository {
    List<Person> findAllPersons();
    void save(Person person);
    boolean update(Person person);
    boolean delete(String firstname, String lastname);
}
