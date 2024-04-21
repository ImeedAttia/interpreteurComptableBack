package com.example.interpreteurcomptable.Service;

import com.example.interpreteurcomptable.Entities.CFE;

import java.util.List;

public interface CFEService {

    CFE addCFE(CFE cvae);
    void deleteCFE(long id);
    CFE updateCFE(long id,CFE cfe);
    List<CFE> getCFE();
    CFE getCFEById(long id);

}
