package com.example.SafetyNet.controller;

import com.example.SafetyNet.model.Person;
import com.example.SafetyNet.service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    //injection du service
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public List<Person> getPerson() {
        return personService.getAllPersons();
    }

    @GetMapping("/personInfo")
    public ResponseEntity<?> getPersonInfo(@RequestParam String lastName) {
        return ResponseEntity.ok(personService.getPersonInfo(lastName));
    }

    @GetMapping("/communityEmail")
    public ResponseEntity<?> getCommunityEmails(@RequestParam String city) {
        return ResponseEntity.ok(personService.getCommunityEmails(city));
    }

    //ajouter une persone
    @PostMapping
    public ResponseEntity<String> addPerson(@RequestBody Person person) {
        personService.addPerson(person);
        return ResponseEntity.ok("person added");
    }

    //Modifier une personne
    @PutMapping
    public ResponseEntity<String> updatePerson(@RequestBody Person person) {
        boolean updated = personService.updatePerson(person);
        if (updated) {
            return ResponseEntity.ok("person updated");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //supprimer une persone
    @DeleteMapping
    public ResponseEntity<String> deletePerson(
            @RequestParam String firstName,
            @RequestParam String lastName) {

        boolean deleted = personService.deletePerson(firstName, lastName);

        if (deleted) {
            return ResponseEntity.ok("Person deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}