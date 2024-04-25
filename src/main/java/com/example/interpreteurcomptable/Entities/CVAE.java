package com.example.interpreteurcomptable.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CVAE {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String denomination;
    String address;
    String codPost;
    String ville;
    String siret;
    String ref;
    Date cessation;
    double valAjoute;
    double refAhiffaffair;
    double refChiffaffairGrp;
    Date date;
    String siren;
}