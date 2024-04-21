package com.example.interpreteurcomptable.Service.Impl;

import com.example.interpreteurcomptable.Entities.CVAE;
import com.example.interpreteurcomptable.Entities.TVA;
import com.example.interpreteurcomptable.Entities.Transaction;
import com.example.interpreteurcomptable.Repository.CVAERepository;
import com.example.interpreteurcomptable.Repository.TVARepository;
import com.example.interpreteurcomptable.Repository.TrasctionRepository;
import com.example.interpreteurcomptable.Service.CVAEService;
import com.example.interpreteurcomptable.Service.TVAService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CVAEServiceImpl implements CVAEService {
    private final CVAERepository cvaeRepository;


    @Override
    public CVAE addCVAE(CVAE cvae) {
        return cvaeRepository.save(cvae);
    }

    @Override
    public void deleteCVAE(long id) {
        cvaeRepository.deleteAll();
    }

    @Override
    public CVAE updateCVAE(long id,CVAE cvae) {
        return cvaeRepository.save(cvae);
    }

    @Override
    public List<CVAE> getCVAE() {
        return cvaeRepository.findAll();
    }

    @Override
    public CVAE getCVAEById(long id) {
        return cvaeRepository.findById(id).orElse(null);
    }
}
