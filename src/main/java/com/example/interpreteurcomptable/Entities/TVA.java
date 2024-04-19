package com.example.interpreteurcomptable.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class TVA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String periodeDeclaration;
    String nomDenomination;
    String adresse;
    String numSiret;
    LocalDate datePaiment;
    String numPhone;
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
}
