package com.example.interpreteurcomptable.Service.Impl;

import com.example.interpreteurcomptable.Entities.Company;
import com.example.interpreteurcomptable.Entities.Transaction;
import com.example.interpreteurcomptable.Repository.CompanyRepository;
import com.example.interpreteurcomptable.Repository.TVARepository;
import com.example.interpreteurcomptable.Repository.TrasctionRepository;
import com.example.interpreteurcomptable.Service.CompanyService;
import com.example.interpreteurcomptable.Service.TrasactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;


    @Override
    public Company addCompany(Company company) {
        return companyRepository.save(company);
    }

}
