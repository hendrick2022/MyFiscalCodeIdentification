package com.pierro.identification.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;

@JsonPropertyOrder({ "surname", "firstName", "birthDate", "gender", "town"})
public class ClientPersonDTO {

    private String surname;
    private String firstName;
    @JsonFormat(pattern="dd-MM-yyyy")
    @ApiModelProperty(notes = "pattern : dd-MM-yyyy",dataType = "LocalDate")
    private String birthDate;
    private String gender;
    private String town;

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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
