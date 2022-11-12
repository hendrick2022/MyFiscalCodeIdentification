package com.pierro.identification.repository;


import com.pierro.identification.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    Person findByIdentificationCode(String idCode);
    Person deleteByIdentificationCode(String idCode);
    Person getByIdentificationCode(String idCode);

}
