package com.example.interpreteurcomptable.Controller;

import com.example.interpreteurcomptable.Entities.CVAE;
import com.example.interpreteurcomptable.Service.CVAEService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/cvae")
@RequiredArgsConstructor
public class CvaeController {
    private final CVAEService cvaeService;

    @PostMapping
    public CVAE createCvae(@RequestBody CVAE cvae){
        return cvaeService.addCVAE(cvae);
    }
    // complete functions
    @GetMapping(path = "/{id}")
    public CVAE getCVAEById(@PathVariable long id){
        return cvaeService.getCVAEById(id);
    }

    @GetMapping
    public List<CVAE> getCVAEList(){
        return cvaeService.getCVAE();
    }
    @DeleteMapping("/{id}")
    public void deleteCVA(@PathVariable long id){
        cvaeService.deleteCVAE(id);
    }
    @PutMapping("/{id}")
    public CVAE updateCVA(@PathVariable long id,@RequestBody CVAE cvae){
        return  cvaeService.updateCVAE(id,cvae);
    }

}
