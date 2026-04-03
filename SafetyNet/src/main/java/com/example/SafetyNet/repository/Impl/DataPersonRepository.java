package com.example.SafetyNet.repository.Impl;

import com.example.SafetyNet.model.Person;
import com.example.SafetyNet.repository.PersonRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class DataPersonRepository implements PersonRepository {

    private final List<Person> persons = new ArrayList<>();

    @Override
    public List<Person> findAllPersons() {
        return persons;
    }

    @Override
    public void save(Person person) {
        persons.add(person);
    }

    @Override
    public boolean update(Person updatedPerson) {
        Optional<Person> existing = persons.stream()
                .filter(p -> p.getFirstName().equals(updatedPerson.getFirstName())
                        && p.getLastName().equals(updatedPerson.getLastName()))
                .findFirst();

        if (existing.isPresent()) {
            Person p = existing.get();
            p.setAddress(updatedPerson.getAddress());
            p.setCity(updatedPerson.getCity());
            p.setZip(updatedPerson.getZip());
            p.setPhone(updatedPerson.getPhone());
            p.setEmail(updatedPerson.getEmail());
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(String firstName, String lastName) {
        return persons.removeIf(p ->
                p.getFirstName().equals(firstName) && p.getLastName().equals(lastName));
    }
    // Ajoute ceci après la méthode delete
    public void setPersons(List<Person> personList) {
        this.persons.clear();
        this.persons.addAll(personList);
    }

}