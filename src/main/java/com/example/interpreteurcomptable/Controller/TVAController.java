package com.example.interpreteurcomptable.Controller;

import com.example.interpreteurcomptable.Entities.Response.UserResponse;
import com.example.interpreteurcomptable.Entities.TVA;
import com.example.interpreteurcomptable.Service.TVAService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/tva")
@RequiredArgsConstructor
public class TVAController {
    private final TVAService tvaService;

    @GetMapping
    public ResponseEntity<TVA> getTVA() {
        return new ResponseEntity<TVA>(tvaService.getTVA(), HttpStatus.OK);
    }
}
