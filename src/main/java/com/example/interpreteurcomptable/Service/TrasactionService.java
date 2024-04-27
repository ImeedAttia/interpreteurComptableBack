package com.example.interpreteurcomptable.Service;

import com.example.interpreteurcomptable.Entities.Transaction;

public interface TrasactionService {
    //crud functs
    Transaction addTransaction(Transaction transaction);

    Transaction updateTransaction(Long id, Transaction transaction);

    Transaction getTransactionById(Long id);

    void deleteTransaction(Long id);

}
