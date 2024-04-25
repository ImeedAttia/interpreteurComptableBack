package com.example.interpreteurcomptable.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    LocalDateTime startDate;
    LocalDateTime endDate;
    String description;
    String accountNumber;
    double amount;
    //Type C or D
    String type;
    String status;
    String details;
    String pending;
    double fee;
    String remarks;
    //    LocalDate datePaiment;
    @ManyToOne
    Company company;
}
