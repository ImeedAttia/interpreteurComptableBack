package com.example.interpreteurcomptable.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String nomDenomination;
    String adresse;
    String numSiret;
    String numPhone;
    @JsonIgnore
    @OneToMany
    List<User> user;
}
