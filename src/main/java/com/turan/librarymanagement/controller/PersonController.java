package com.turan.librarymanagement.controller;

import com.turan.librarymanagement.domain.Person;
import com.turan.librarymanagement.dto.BookDTO;
import com.turan.librarymanagement.service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {
    private  final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MEMBER')")
    public ResponseEntity<List<Person>> getPersons(@RequestParam("city") String city, @RequestParam("psc") String postalCode){
        List<Person> persons =  personService.findPeopleInCityWithPostalCode(city,postalCode);
        return ResponseEntity.ok(persons);
    }
    @GetMapping("/x")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MEMBER')")
    public ResponseEntity<List<Person>> getPersons2(@RequestParam("city") String city, @RequestParam("psc") String postalCode){
        List<Person> persons =  personService.findPeopleInCityWithPostalCode2(city,postalCode);
        return ResponseEntity.ok(persons);
    }
    @GetMapping("/y/{name}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MEMBER')")
    public ResponseEntity<List<Person>> getPersons3(@PathVariable("name") String name){
        List<Person> persons =  personService.getPeopleByName(name);
        return ResponseEntity.ok(persons);
    }
     @GetMapping("/z/{name}/{postalCode}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MEMBER')")
    public ResponseEntity<List<Person>> getPersons4(@PathVariable("name") String name,@PathVariable("postalCode") String postalCode){
        List<Person> persons =  personService.findPeopleInCityWithPostalCode4(name,postalCode);
        return ResponseEntity.ok(persons);
    }

    @GetMapping("/k/{name}/{postalCode}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MEMBER')")
    public ResponseEntity<List<Person>> getPersons5(@PathVariable("name") String name,@PathVariable("postalCode") String postalCode){
        List<Person> persons =  personService.findPeopleInCityWithPostalCode5(name,postalCode);
        return ResponseEntity.ok(persons);
    }


}

