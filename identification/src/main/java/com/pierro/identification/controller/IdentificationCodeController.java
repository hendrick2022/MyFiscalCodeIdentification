package com.pierro.identification.controller;
import com.pierro.identification.entity.Person;
import com.pierro.identification.entity.PersonDTO;
import com.pierro.identification.repository.PersonRepository;
import com.pierro.identification.repository.TownRepository;
import com.pierro.identification.service.IdentificationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/code")
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
    public ResponseEntity<PersonDTO> firstPartCode(@RequestBody PersonDTO personDto) {
        Person person = service.convertToEntity(personDto,"");
        if (townRepo.findByTownName(person.getTown()) != null){
            String code = service.getTheCode(person);
            person.setIdentificationCode(code);
            if(personRepo.findByIdentificationCode(code) == null) {
                personRepo.save(person);
                return ResponseEntity.status(HttpStatus.CREATED).body(service.convertToDto(person, "SUCCESSFULLY CREATED NEW PERSON"));
            }
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(service.convertToDto(person,"THIS PERSON IS ALREADY REGISTER"));
        }else{

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(service.convertToDto(person,"CHECK THE SPELLING OF TOWN NO SUCH TOWN IN OUR DATA BASE"));
        }
    }



    @GetMapping("/persons")
    public ResponseEntity<?> getPersons() {
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
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
        }
    }


        @GetMapping("/person/{id}")
    public ResponseEntity<PersonDTO> get(@PathVariable int id) {
        try {
            Person person = personRepo.findById(id).orElse(null);
            if (Objects.nonNull(person)){
                return ResponseEntity.status(HttpStatus.OK).body(service.convertToDto(person, "HERE HE IS"));
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PersonDTO("NOBODY WITH THIS ID:  " + id + "   FOR NOW"));
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }



    @PutMapping("/person/{id}")
    public ResponseEntity<PersonDTO> updatePerson(@RequestBody PersonDTO personDto, @PathVariable int id) {
        Person updatePerson = service.convertToEntity(personDto,"");
        if (Objects.nonNull(townRepo.findByTownName(updatePerson.getTown())) ){
            updatePerson.setIdentificationCode(service.getTheCode(updatePerson));
            try {
                Person person = personRepo.findById(id).orElse(null);
                if (Objects.nonNull(person)){
                    person.setIdentificationCode(service.getTheCode(updatePerson));
                    person.setTown(updatePerson.getTown());
                    person.setBirthDate(updatePerson.getBirthDate());
                    person.setFirstName(updatePerson.getFirstName());
                    person.setSurname(updatePerson.getSurname());
                    person.setGender(updatePerson.getGender());
                    personRepo.save(person);
                    return ResponseEntity.status(HttpStatus.OK).body(service.convertToDto(person, "THIS PERSON HAS BEEN SUCCESSFULLY UPDATED"));
                }else {
                    if(personRepo.findByIdentificationCode(service.getTheCode(updatePerson)) == null) {
                        personRepo.save(updatePerson);
                        return ResponseEntity.status(HttpStatus.CREATED).body(service.convertToDto(updatePerson, "THIS PERSON HAS BEEN CREATED"));
                    }else{
                        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(service.convertToDto(updatePerson, "THIS PERSON ALREADY EXIST"));
                    }
                }
            } catch (NoSuchElementException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(service.convertToDto(updatePerson,"CHECK THE SPELLING OF TOWN NO SUCH TOWN IN OUR DATA BASE"));
        }
    }



    @DeleteMapping("/person/{id}")
    public ResponseEntity<PersonDTO> deleting(@PathVariable int id) {
        try {
            Person person = personRepo.findById(id).orElse(null);
            if (Objects.nonNull(person)){
                PersonDTO thisPerson = service.convertToDto(person, "THIS PERSON HAS SUCCESSFULLY BEEN DELETED");
                personRepo.deleteById(id);
                return ResponseEntity.status(HttpStatus.OK).body(thisPerson);
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PersonDTO(id,"NOBODY WITH THIS ID FOR NOW"));
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }



    @DeleteMapping("/persons")
    public ResponseEntity<PersonDTO> deletingAll() {
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


