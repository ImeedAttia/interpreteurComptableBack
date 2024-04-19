package com.example.interpreteurcomptable.Service.Impl;

import com.example.interpreteurcomptable.Entities.TVA;
import com.example.interpreteurcomptable.Entities.Transaction;
import com.example.interpreteurcomptable.Repository.TVARepository;
import com.example.interpreteurcomptable.Repository.TrasctionRepository;
import com.example.interpreteurcomptable.Service.TVAService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TVAServiceImpl implements TVAService {
    private final TVARepository tvaRepository;
    private final TrasctionRepository trasctionRepository;
    @Override
    public TVA addTVA(TVA tva) {
        TVA tva1 = new TVA();
        List<Transaction> transaction = trasctionRepository.findAll();
        List<Transaction> transactionFiltered = transaction.stream().filter(t -> t.getType().equals("C")).toList();
        double sumOfAmounts = transactionFiltered.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
        tva1.setVente(sumOfAmounts);
        tva1.setTvaBrute20(sumOfAmounts * 0.2);
        tva1.setTvaBrute10(sumOfAmounts * 0.1);
        tva1.setTvaBrute55(sumOfAmounts * 0.055);
        return tvaRepository.save(tva);
    }

    @Override
    public void deleteTVA() {

    }

    @Override
    public void updateTVA() {

    }

    @Override
    public void getTVA() {

    }
}
