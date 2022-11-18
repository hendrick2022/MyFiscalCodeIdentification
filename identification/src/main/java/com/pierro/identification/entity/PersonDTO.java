package com.pierro.identification.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;

//@Datalombok
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


    public PersonDTO() {
    }

    public PersonDTO(String message) {
        this.message = message;
    }

    public PersonDTO(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }


    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "PersonDTOInfo{" +
                ", Surname='" + surname + '\'' +
                ", firstName='" + firstName + '\'' +
                ", town='" + town + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
