package com.example.interpreteurcomptable.Repository;

import com.example.interpreteurcomptable.Entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrasctionRepository extends JpaRepository<Transaction,Long> {
}
