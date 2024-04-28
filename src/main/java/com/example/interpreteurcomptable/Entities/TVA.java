package com.example.interpreteurcomptable.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TVA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    double vente;
    double aOI;
    double tvaBrute20;
    double tvaBrute10;
    double tvaBrute55;
    double totTvaBruteDue;
    double aBService;
    double totTvaDed;
    double totTvaDue;
    double tvaNetDue;
    double taxAss;
    double totPayer;
    Date creationDate;
    String du;
    String au;
    String nom;
    String address;
    String siret;
    int phone;
}
