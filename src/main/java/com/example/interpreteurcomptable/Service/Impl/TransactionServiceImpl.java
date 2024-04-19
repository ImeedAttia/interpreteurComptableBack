package com.example.interpreteurcomptable.Service.Impl;

import com.example.interpreteurcomptable.Entities.TVA;
import com.example.interpreteurcomptable.Entities.Transaction;
import com.example.interpreteurcomptable.Repository.TVARepository;
import com.example.interpreteurcomptable.Repository.TrasctionRepository;
import com.example.interpreteurcomptable.Service.TrasactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TrasactionService {
    private final TrasctionRepository trasctionRepository;
    private final TVARepository tvaRepository;

    @Override
    public Transaction addTransaction(Transaction transaction) {
        return trasctionRepository.save(transaction);
    }
}
