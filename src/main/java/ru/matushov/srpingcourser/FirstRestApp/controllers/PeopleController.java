package ru.matushov.srpingcourser.FirstRestApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.matushov.srpingcourser.FirstRestApp.models.Person;
import ru.matushov.srpingcourser.FirstRestApp.services.PeopleService;
import ru.matushov.srpingcourser.FirstRestApp.util.PersonErrorResponse;
import ru.matushov.srpingcourser.FirstRestApp.util.PersonNotCreatedException;
import ru.matushov.srpingcourser.FirstRestApp.util.PersonNotFoundException;

import javax.validation.Valid;
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

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid Person person,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField()).append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }

            throw new PersonNotCreatedException(errorMsg.toString());


        }

        peopleService.save(person);

        //отправляем HTTP ответ с пустым телом и со статусом 200
        return ResponseEntity.ok(HttpStatus.OK);

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
    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotCreatedException e) {
        PersonErrorResponse response = new PersonErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        //In HTTP Response (response) and status in header
        return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST); //NOT_FOUND - 404

    }
}
