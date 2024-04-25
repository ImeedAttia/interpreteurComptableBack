package com.example.interpreteurcomptable.Controller;

import com.example.interpreteurcomptable.Entities.CFE;
import com.example.interpreteurcomptable.Service.CFEService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/cfe")
@RequiredArgsConstructor
public class CFEController {

    private final CFEService cvaeService;

    @PostMapping
    public CFE createCvae(@RequestBody CFE cfe) {
        return cvaeService.addCFE(cfe);
    }

    // complete functions
    @GetMapping(path = "/{id}")
    public CFE getCFEById(@PathVariable long id) {
        return cvaeService.getCFEById(id);
    }

    @GetMapping
    public List<CFE> getCFEList() {
        return cvaeService.getCFE();
    }

    @DeleteMapping("/{id}")
    public void deleteCVA(@PathVariable long id) {
        cvaeService.deleteCFE(id);
    }

    @PutMapping("/{id}")
    public CFE updateCVA(@PathVariable long id, @RequestBody CFE cfe) {
        return cvaeService.updateCFE(id, cfe);
    }
}
