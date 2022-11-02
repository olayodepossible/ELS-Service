package com.possible.elasticsearch.service;

import com.possible.elasticsearch.document.Person;
import com.possible.elasticsearch.repository.PersonRepository;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person savePerson(final Person person){
       return personRepository.save(person);

    }

    public Person findById(String id){
        return personRepository.findById(id).orElse(null);

    }
}
