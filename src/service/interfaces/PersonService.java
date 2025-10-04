package main.service.interfaces;

import main.model.Person;

import java.util.Map;

public interface PersonService {
    Person findPerson(Integer id);
    Person updatePerson(Integer id, Map<String , Object> updates);
}
