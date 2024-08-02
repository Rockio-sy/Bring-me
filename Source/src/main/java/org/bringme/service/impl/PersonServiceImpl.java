package org.bringme.service.impl;

import org.bringme.model.Person;
import org.bringme.repository.PersonRepository;
import org.bringme.service.PersonService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public List<Person> getAllPersons() {
        return personRepository.getAll();
    }

    @Override
    public Person getPersonById(Long id) {
        Optional<Person> newPerson = personRepository.getById(id);
        return newPerson.orElse(null);
    }

    @Override
    public Person savePerson(Person person) {
        int rowAffected = personRepository.savePerson(person);
        if (rowAffected > 0) {
            return person;
        }
        return null;
    }

    @Override
    public Person updatePerson(Person person) {
        Optional<Person> foundedPerson = personRepository.getPersonByPhone(person.getPhone());
        if (foundedPerson.isPresent()) {
            int rowAffected = personRepository.updatePerson(foundedPerson.get());
            if (rowAffected > 0) {
                return person;
            }
        }
        return null;
    }

    @Override
    public Person getPersonByPhone(String phone) {
        Optional<Person> person = personRepository.getPersonByPhone(phone);
        return person.orElse(null);
    }

    @Override
    public void deletePerson(Long id) {
        personRepository.deleteById(id);
    }

}
