package com.example.interpreteurcomptable.Repository;

import com.example.interpreteurcomptable.Entities.Company;
import com.example.interpreteurcomptable.Entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company,Long> {
    Company findByUserId(Long userId);
}
