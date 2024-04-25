package com.example.interpreteurcomptable.Service.Impl;

import com.example.interpreteurcomptable.Entities.CFE;
import com.example.interpreteurcomptable.Repository.CFERepository;
import com.example.interpreteurcomptable.Service.CFEService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CFEServiceImpl implements CFEService {
    private final CFERepository cfeRepository;

    @Override
    public CFE addCFE(CFE cfe) {
        return cfeRepository.save(cfe);
    }

    @Override
    public void deleteCFE(long id) {
        cfeRepository.deleteById(id);
    }

    @Override
    public CFE updateCFE(long id, CFE cfe) {
//        CFE cfeDB = cfeRepository.findById(id).orElse(null);
//        cfeDB.set
        return cfeRepository.save(cfe);
    }

    @Override
    public List<CFE> getCFE() {
        return cfeRepository.findAll();
    }

    @Override
    public CFE getCFEById(long id) {
        return cfeRepository.findById(id).orElse(null);
    }
}
