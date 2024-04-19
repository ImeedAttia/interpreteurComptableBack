package com.example.interpreteurcomptable.Service;

import com.example.interpreteurcomptable.Entities.TVA;

public interface TVAService {
    TVA addTVA(TVA tva);
    void deleteTVA();
    void updateTVA();
    void getTVA();
}
