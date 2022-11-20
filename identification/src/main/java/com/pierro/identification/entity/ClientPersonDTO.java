package com.pierro.identification.entity;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;

@Setter
@Getter
@Builder
@AllArgsConstructor
@JsonPropertyOrder({ "surname", "firstName", "birthDate", "gender", "town"})
public class ClientPersonDTO {
    private String surname;
    private String firstName;
    @JsonFormat(pattern="dd-MM-yyyy")
    @ApiModelProperty(notes = "pattern : dd-MM-yyyy",dataType = "LocalDate")
    private String birthDate;
    private String gender;
    private String town;


}
