package ru.matushov.srpingcourser.FirstRestApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.matushov.srpingcourser.FirstRestApp.models.Person;
import ru.matushov.srpingcourser.FirstRestApp.services.PeopleService;
import ru.matushov.srpingcourser.FirstRestApp.util.PersonErrorResponse;
import ru.matushov.srpingcourser.FirstRestApp.util.PersonNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;

    @Autowired
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping()
    public List<Person> getPeople() {
        return peopleService.findAll(); //Jackson convert all objects to JSON
    }

    @GetMapping("/{id}")
    public Person getPerson(@PathVariable("id") int id){
        return peopleService.findOne(id); //Jackson convert one object to JSON
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e) {
        PersonErrorResponse response = new PersonErrorResponse(
                "Person with this id wasn't found!",
                System.currentTimeMillis()
        );

        //In HTTP Response (response) and status in header
        return new ResponseEntity<>(response , HttpStatus.NOT_FOUND); //NOT_FOUND - 404

    }
}
