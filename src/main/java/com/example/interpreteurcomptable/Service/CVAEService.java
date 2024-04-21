package com.example.interpreteurcomptable.Service;

import com.example.interpreteurcomptable.Entities.CVAE;
import com.example.interpreteurcomptable.Entities.Company;

import java.util.List;

public interface CVAEService {
    //crud functs
    CVAE addCVAE(CVAE cvae);
    void deleteCVAE(long id);
    CVAE updateCVAE(long id,CVAE cvae);
    List<CVAE> getCVAE();
    CVAE getCVAEById(long id);

}
