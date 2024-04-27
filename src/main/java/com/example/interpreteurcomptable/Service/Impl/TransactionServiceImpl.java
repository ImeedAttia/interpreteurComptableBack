package com.example.interpreteurcomptable.Service.Impl;

import com.example.interpreteurcomptable.Entities.TVA;
import com.example.interpreteurcomptable.Entities.Transaction;
import com.example.interpreteurcomptable.Repository.TVARepository;
import com.example.interpreteurcomptable.Repository.TrasctionRepository;
import com.example.interpreteurcomptable.Service.TrasactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TrasactionService {
    private final TrasctionRepository trasctionRepository;

    @Override
    public Transaction addTransaction(Transaction transaction) {
        return trasctionRepository.save(transaction);
    }

    @Override
    @Transactional
    public Transaction updateTransaction(Long id, Transaction transactionDetails) {
        Transaction transaction = trasctionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id " + id));

        transaction.setStartDate(transactionDetails.getStartDate());
        transaction.setEndDate(transactionDetails.getEndDate());
        transaction.setDescription(transactionDetails.getDescription());
        transaction.setAccountNumber(transactionDetails.getAccountNumber());
        transaction.setAmount(transactionDetails.getAmount());
        transaction.setType(transactionDetails.getType());
        transaction.setStatus(transactionDetails.getStatus());
        transaction.setDetails(transactionDetails.getDetails());
        transaction.setPending(transactionDetails.getPending());
        transaction.setFee(transactionDetails.getFee());
        transaction.setRemarks(transactionDetails.getRemarks());

        return trasctionRepository.save(transaction);
    }

    @Override
    public Transaction getTransactionById(Long id) {
        return trasctionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id " + id));
    }

    @Override
    public void deleteTransaction(Long id) {
        Transaction transaction = getTransactionById(id); // Reuse the get method to handle the exception
        trasctionRepository.delete(transaction);
    }
}