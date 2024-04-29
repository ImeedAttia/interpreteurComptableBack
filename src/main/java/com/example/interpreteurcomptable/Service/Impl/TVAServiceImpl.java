package com.example.interpreteurcomptable.Service.Impl;

import com.example.interpreteurcomptable.Entities.TVA;
import com.example.interpreteurcomptable.Entities.Transaction;
import com.example.interpreteurcomptable.Repository.TVARepository;
import com.example.interpreteurcomptable.Repository.TrasctionRepository;
import com.example.interpreteurcomptable.Service.TVAService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TVAServiceImpl implements TVAService {
    private final TVARepository tvaRepository;
    private final TrasctionRepository trasctionRepository;
    @Override
    public TVA addTVA(TVA tva) {
        tva.setCreationDate(new Date());
        return tvaRepository.save(tva);
    }

    @Override
    public void deleteTVA() {

    }

    @Override
    public void updateTVA() {

    }

    @Override
    public TVA getTVA() {
        TVA tvacalculated = calculateTVA();
        return tvacalculated;
    }


    private TVA calculateTVA(){
        TVA tva = new TVA();
        List<Transaction> transactions = trasctionRepository.findAll();

        // It's important to handle the case where there might be no transactions
        if (transactions.isEmpty()) {
            throw new IllegalStateException("No transactions available");
        }

        List<Transaction> creditTransactions = transactions.stream()
                .filter(t -> t.getType().equals("C"))
                .toList();

        double sumOfAmountsCredit = creditTransactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();

        double sumOfAmountsDebit = transactions.stream()
                .filter(t -> t.getType().equals("D"))
                .mapToDouble(Transaction::getAmount)
                .sum();

        // Assuming the list has at least one transaction and they are chronologically sorted
        Date startDate = transactions.get(0).getStartDate(); // Start date of the first transaction
        Date endDate = transactions.get(transactions.size() - 1).getEndDate(); // End date of the last transaction

        tva.setDu(startDate.toString());
        tva.setAu(endDate.toString());
        tva.setVente(sumOfAmountsCredit);
        tva.setTvaBrute20(sumOfAmountsCredit * 0.2);
        tva.setTvaBrute10(sumOfAmountsCredit * 0.1);
        tva.setTvaBrute55(sumOfAmountsCredit * 0.055);
        tva.setAOI(sumOfAmountsDebit);
        tva.setTotTvaBruteDue(tva.getTvaBrute20() + tva.getTvaBrute10() + tva.getTvaBrute55());

        tva.setABService(tva.getTotTvaBruteDue() - tva.getAOI());
        tva.setTotTvaDed(tva.getTvaBrute20() + tva.getTvaBrute10() + tva.getTvaBrute55());
        tva.setTotTvaDue(tva.getTotTvaBruteDue() - tva.getTotTvaDed());
        tva.setTvaNetDue(tva.getTotTvaDue() + tva.getABService());
        tva.setTaxAss(tva.getTvaNetDue() * 0.2);
        tva.setTotPayer(tva.getTvaNetDue() + tva.getTaxAss());
        return tva;
    }


}
