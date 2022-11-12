package com.pierro.identification.service;
import com.pierro.identification.entity.Person;
import com.pierro.identification.entity.PersonDTO;
import com.pierro.identification.repository.PersonRepository;
import com.pierro.identification.repository.TownRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class IdentificationService {

    @Autowired
    TownRepository repo;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    PersonRepository PersonRepo;


    public String[] separation(String givenString) {
        givenString = givenString.replaceAll("(?i)[^A-Z]", "");
        String consonants = givenString.replaceAll("(?i)[aeiou]", "");
        String vowels = givenString.replaceAll("(?i)[^aeiou]", "");
        return new String[]{consonants, vowels};
    }

    


    public String firstNameCode(String[] stringArray){
        String consonants = stringArray[0];
        String vowels = stringArray[1];
        String code = "";

        if (consonants.length() == 3 ) {
            code = consonants.substring(0, 3);
        }else if(consonants.length() > 3){
            code = consonants.substring(0, 1) + consonants.substring(2,4);
        } else if((consonants.length()+ vowels.length())>=3 && consonants.length() < 3){
            switch (consonants.length()) {
                case 2:
                    code = consonants + vowels.substring(0, 1);
                    break;
                case 1:
                    code = consonants + vowels.substring(0, 2);
                    break;
                case 0:
                    code = vowels.substring(0, 3);
                    break;
            }
        }else if((consonants.length()+ vowels.length()) < 3){
            switch ((consonants.length()+ vowels.length())){
                case 2:
                    code = consonants + vowels + "X";
                    break;
                case 1:
                    code = consonants + vowels + "XX";
                    break;
                case 0:
                    code = "XXX";
                    break;
            }
        }

        return code.toUpperCase();

    }



    public String surnameCode(String[] stringArray){
        String consonants = stringArray[0];
        String vowels = stringArray[1];
        String code = "";
        if (consonants.length() >= 3 ) {
            code = consonants.substring(0, 3);
        }else if((consonants.length()+ vowels.length())>=3 && consonants.length() < 3){
            switch (consonants.length()) {
                case 2:
                    code = consonants + vowels.substring(0, 1);
                    break;
                case 1:
                    code = consonants + vowels.substring(0, 2);
                    break;
                case 0:
                    code = vowels.substring(0, 3);
                    break;
            }
        }else if((consonants.length()+ vowels.length()) < 3){
            switch ((consonants.length()+ vowels.length())){
                case 2:
                    code = consonants + vowels + "X";
                    break;
                case 1:
                    code = consonants + vowels + "XX";
                    break;
                case 0:
                    code = "XXX";
                    break;
            }
        }

        return code.toUpperCase();
    }



    public String codeLetter(String firstPartCode){
        int number = 0;
        int index = 0 ;
        int[] evenCharacterCode = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,11,12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        char[] letter = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        Character[] character = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int[] oddCharacterCode = {1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 2, 4, 18, 20, 11, 3, 6, 8, 12, 14, 16, 10, 22, 25, 24, 23, 1, 0, 5, 7, 9, 13, 15, 17, 19, 21};
        char[] entry = firstPartCode.toLowerCase().toCharArray();
       for (int i = 0; i<firstPartCode.length() ; i++){
            index = Arrays.asList(character).indexOf(entry[i]);
            if(i%2 ==0){
                number += oddCharacterCode[index];
            }else {
                number += evenCharacterCode[index];
            }
        }
        return letter[number%26]+"";
    }



    public String birthDayCode(String birthDate, String gender){
        gender = gender.toLowerCase();
        if(gender.equals("male") ){
           return birthDate;
        }else {
            return (Integer.parseInt(birthDate ) +40) + "";
        }
    }



    public String monthLetter(String  monthOfBirth) {
        int index = 0;
        int[] month = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        String[] letterForMonth = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"};
        for (int j = 0; j < month.length; j++) {
            if (month[j] ==Integer.parseInt(monthOfBirth)){
                index = j;
            }
        }
        return letterForMonth[index];
    }

    public String getTheCode(Person person){

        String  date = (person.getBirthDate()).toString();
        String firstPartCode = surnameCode(separation(person.getSurname())) + firstNameCode(separation(person.getFirstName())) +
                (date.substring(2,4) + monthLetter(date.substring(5,7))+ birthDayCode(date.substring(8,10),person.getGender()) +
                repo.findByTownName(person.getTown()).getTownCode());
        String code = firstPartCode + codeLetter(firstPartCode);

        return code;
    }


    public Person convertToEntity(PersonDTO personDto, String idCode){
        Person myPerson = modelMapper.map(personDto, Person.class);
        myPerson.setIdentificationCode(idCode);
        return myPerson;
    }


    public PersonDTO convertToDto(Person person, String message){
        PersonDTO myPerson = modelMapper.map(person, PersonDTO.class);
        myPerson.setMessage(message);
        return myPerson;
    }

    public ResponseEntity<PersonDTO> setUpdate(Person updatePerson, int id){
        updatePerson.setIdentificationCode(getTheCode(updatePerson));
        try {
            Person person = PersonRepo.findById(id).orElse(null);
            if (Objects.nonNull(person)){
                person.setTown(updatePerson.getTown());
                person.setBirthDate(updatePerson.getBirthDate());
                person.setFirstName(updatePerson.getFirstName());
                person.setSurname(updatePerson.getSurname());
                person.setGender(updatePerson.getGender());
                PersonRepo.save(person);
                return ResponseEntity.status(HttpStatus.OK).body(convertToDto(person, "THIS PERSON HAS BEEN SUCCESSFULLY UPDATED"));
            }else {
                if(PersonRepo.findByIdentificationCode(getTheCode(updatePerson)) == null) {
                    PersonRepo.save(updatePerson);
                    return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(updatePerson, "THIS PERSON HAS BEEN CREATED"));
                }else{
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(convertToDto(updatePerson, "THIS PERSON ALREADY EXIST"));
                }
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
}
