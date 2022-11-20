package com.pierro.identification.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;
import lombok.*;
import javax.persistence.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
@Entity
@Table(name = "codeTown")
@JsonPropertyOrder({"townCode", "townName"})
//EnableAutoConfiguration
public class Town {

    @Id
    @Column(name = "town_name")
    @Setter @Getter
    private String townName;
    @Setter @Getter
    @Column(name = "town_code")
    private String townCode;

}
