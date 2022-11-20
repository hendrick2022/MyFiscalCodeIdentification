package com.pierro.identification.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.stereotype.Component;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
@Entity

public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String identificationCode;
    private String Surname;
    private String firstName;
    @JsonFormat(pattern="dd-MM-yyyy")
    private String birthDate;
    private String gender;
    private String town;

   public Person(Long id) {
        this.id = id;
    }

}

