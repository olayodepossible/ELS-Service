package com.possible.elasticsearch.controller;

import com.possible.elasticsearch.document.Person;
import com.possible.elasticsearch.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/person")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<Person> saveDocument(@RequestBody Person person){
        return new ResponseEntity<>(personService.savePerson(person), HttpStatus.CREATED);

    }

    @GetMapping("/{id}")
    public Person findDocumentById(@PathVariable String id){
        return personService.findById(id);

    }
}
