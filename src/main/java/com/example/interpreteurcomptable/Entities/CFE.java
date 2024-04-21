package com.example.interpreteurcomptable.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CFE {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String departement;
    String commune;
    String nom;
    String activites;
    String adresse;
    String siret;
    String nace;
    String nomComp;
    String addComp;
    String phoneComp;
    String mailComp;
}