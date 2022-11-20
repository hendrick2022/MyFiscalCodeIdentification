package com.pierro.identification.entity;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@JsonPropertyOrder({"message","id", "surname", "firstName", "birthDate", "gender", "town"})
public class PersonDTO {
    @JsonProperty
    private int id;
    @JsonProperty
    private String surname;
    @JsonProperty
    private String firstName;
    @JsonProperty
    @JsonFormat(pattern="dd-MM-yyyy")
    @ApiModelProperty(notes = "pattern : dd-MM-yyyy",dataType = "LocalDate")
    private String birthDate;
    private String gender;
    private String town;
    private String message;


    public PersonDTO(String message) {
       this.message = message;
    }

    public PersonDTO(int id, String message) {
        this.id = id;
        this.message = message;
    }

}
