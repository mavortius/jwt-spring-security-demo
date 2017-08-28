package org.zerhusen.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerhusen.PersonRepository;
import org.zerhusen.model.Person;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PersonController {
    private final PersonRepository repository;

    public PersonController(PersonRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/persons")
    public List<Person> getPersons() {
        return repository.findAll();
    }

    @GetMapping("/persons/{cpf}")
    public ResponseEntity<Person> getPerson(@PathVariable String cpf) {
        Person person = repository.findByCpf(cpf);

        return person != null ? ResponseEntity.ok(person) : ResponseEntity.notFound().build();
    }
}
