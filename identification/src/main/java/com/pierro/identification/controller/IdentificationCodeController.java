package com.pierro.identification.controller;

import com.pierro.identification.entity.ClientPersonDTO;
import com.pierro.identification.entity.Person;
import com.pierro.identification.entity.PersonDTO;
import com.pierro.identification.repository.PersonRepository;
import com.pierro.identification.repository.TownRepository;
import com.pierro.identification.service.IdentificationService;
import org.apache.commons.validator.GenericValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class IdentificationCodeController {

    @Autowired
    TownRepository townRepo;
    @Autowired
    PersonRepository personRepo;

    @Autowired
    IdentificationService service;

    @Autowired
    ModelMapper modelMapper;


    @PostMapping ("/createPerson")
    public ResponseEntity<PersonDTO> registerNewPerson(@RequestBody ClientPersonDTO personDto) {
        if (!(GenericValidator.isDate(personDto.getBirthDate(), "dd-MM-yyyy", true))){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new PersonDTO("wrong entry Date pattern type not respected"));
        }
        Person person = service.convertToEntity(personDto,"");
        if (townRepo.findByTownName(person.getTown()) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(service.convertToDto(person, "check the spelling of town no such town in the data base"));
        }
        person.setIdentificationCode(service.getTheCode(person));
        if(personRepo.findByIdentificationCode(service.getTheCode(person)) == null) {
            personRepo.save(person);
            return ResponseEntity.status(HttpStatus.CREATED).body(service.convertToDto(person, "successfully created new person"));
        }
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(service.convertToDto(person,"this person is already register"));
    }



    @GetMapping("/persons")
    public ResponseEntity<?> getAllPersons() {
        try {
            List<Person> persons = personRepo.findAll();
            ArrayList<PersonDTO> personDtos = new ArrayList<PersonDTO>();
            if (Objects.nonNull(persons)) {
                persons.stream().forEach(person -> { personDtos.add(service.convertToDto(person,"person Number  " + person.getId()+ "")); });
                return ResponseEntity.status(HttpStatus.OK).body(personDtos);
            } else
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new PersonDTO("THE LIST IS EMPTY FOR NOW"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


        @GetMapping("/person/{id}")
    public ResponseEntity<PersonDTO> getPersonById(@PathVariable int id) {
        try {
            Person person = personRepo.findById(id).orElse(null);
            if (Objects.nonNull(person)){
                return ResponseEntity.status(HttpStatus.OK).body(service.convertToDto(person, "HERE HE IS"));
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PersonDTO("NOBODY WITH THIS ID:  " + id + "   FOR NOW"));
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    @PutMapping("/person/{id}")
    public ResponseEntity<PersonDTO> updatePersonById(@RequestBody ClientPersonDTO personDto, @PathVariable int id) {
        if (!(GenericValidator.isDate(personDto.getBirthDate(), "dd-MM-yyyy", true))){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new PersonDTO("wrong entry Date pattern type not respected"));
        }
        Person updatePerson = service.convertToEntity(personDto,"");
        if (!Objects.nonNull(townRepo.findByTownName(updatePerson.getTown())) ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(service.convertToDto(updatePerson,"CHECK THE SPELLING OF TOWN NO SUCH TOWN IN OUR DATA BASE"));
        }
        return service.setUpdate(updatePerson, id);
    }



    @DeleteMapping("/person/{id}")
    public ResponseEntity<PersonDTO> deletingPersonById(@PathVariable int id) {
        try {
            Person person = personRepo.findById(id).orElse(null);
            if (Objects.nonNull(person)){
                personRepo.deleteById(id);
                return ResponseEntity.status(HttpStatus.OK).body(service.convertToDto(person, "THIS PERSON HAS SUCCESSFULLY BEEN DELETED"));
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PersonDTO(id,"NOBODY WITH THIS ID FOR NOW"));
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    @DeleteMapping("/persons")
    public ResponseEntity<PersonDTO> emptyingDataBase() {
        personRepo.deleteAll();
        return ResponseEntity.status(HttpStatus.OK).body(new PersonDTO("EMPTY DATABASE NOW"));
    }


    /**

     @GetMapping("/town/{townName}")
     public String findTown(@PathVariable String townName) {
     try {
     return townRepo.findByTownName(townName).getTownCode();
     }catch (NoSuchElementException e){
     return "no town with such and entry in our data base";
     }
     }
     @GetMapping("/loadTownsForeign")
     public String loadForeignTowns() {
     theFile.loadTownsInDB("E:\\learnSpringBoot\\learnSpringBoot\\foreignTownCode.csv");

     return "files successfully loaded";
     }


/**
     @GetMapping("/loadTownsItaly")
     public String loadItalyTowns() {
     theFile.loadTownsInDB("E:\\learnSpringBoot\\learnSpringBoot\\CodeTownItaly.csv");

     return "files successfully loaded";
     }

     **/

}


