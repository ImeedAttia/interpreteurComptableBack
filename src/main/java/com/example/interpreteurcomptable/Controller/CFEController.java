package com.example.interpreteurcomptable.Controller;

import com.example.interpreteurcomptable.Entities.CFE;
import com.example.interpreteurcomptable.Entities.FileEntity;
import com.example.interpreteurcomptable.Entities.Historique;
import com.example.interpreteurcomptable.Entities.TVA;
import com.example.interpreteurcomptable.Repository.HistoriqueRepository;
import com.example.interpreteurcomptable.Service.CFEService;
import com.example.interpreteurcomptable.Service.FileService;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/cfe")
@RequiredArgsConstructor
public class CFEController {

    private final CFEService cfeService;
    private final FileService fileService;
    private final HistoriqueRepository historiqueRepository;

    @PostMapping
    public CFE createCvae(@RequestBody CFE cfe) {
        return cfeService.addCFE(cfe);
    }

    // complete functions
    @GetMapping(path = "/{id}")
    public CFE getCFEById(@PathVariable long id) {
        return cfeService.getCFEById(id);
    }

    @GetMapping
    public List<CFE> getCFEList() {
        return cfeService.getCFE();
    }

    @DeleteMapping("/{id}")
    public void deleteCVA(@PathVariable long id) {
        cfeService.deleteCFE(id);
    }

    @PutMapping("/{id}")
    public CFE updateCVA(@PathVariable long id, @RequestBody CFE cfe) {
        return cfeService.updateCFE(id, cfe);
    }



    @PostMapping("/fill-pdf/{cfeId}/{userId}")
    public ResponseEntity<byte[]> fillPdfAndDownload(@RequestParam("file") MultipartFile file, @PathVariable long cfeId,@PathVariable long userId) {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
            if (acroForm == null) {
                return ResponseEntity.badRequest().body("The provided PDF file does not contain a form.".getBytes());
            }
            // Fetch the CFE data
            CFE cfeData = cfeService.getCFEById(cfeId);
            if (cfeData == null) {
                return ResponseEntity.notFound().build();
            }
            fillFields(acroForm, cfeData);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream);
            byte[] pdfBytes = byteArrayOutputStream.toByteArray();
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            MultipartFile multipartFile = new MockMultipartFile("file", "CFE"+ dateFormat.format(now)+".pdf", "application/pdf", pdfBytes);
            CFE cfe1 = cfeService.addCFE(cfeData);
            FileEntity feInput = fileService.storeFilePdf(file, userId);
            FileEntity feOutput = fileService.storeFilePdf(multipartFile, userId);
            Historique historique = new Historique();
            historique.setCfe(cfe1);
            historique.setCreatedAt(now);
            historique.setUpdatedAt(now);
            historique.setTitre("CFE - "+ dateFormat.format(now));
            historique.setInputFile(feInput);
            historique.setOutputFile(feOutput);
            historiqueRepository.save(historique);


            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=CFE.pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(("Error processing PDF file: " + e.getMessage()).getBytes());
        }
    }


    private void fillFields(PDAcroForm acroForm, CFE cfe) throws IOException {
        acroForm.getField("dep").setValue(cfe.getDepartement());
        acroForm.getField("commune").setValue(cfe.getCommune());
        acroForm.getField("nom").setValue(cfe.getNom());
        acroForm.getField("act_ex").setValue(cfe.getActivites());
        acroForm.getField("add_commune").setValue(cfe.getAdresse());
        acroForm.getField("siret").setValue(cfe.getSiret());
        acroForm.getField("nace").setValue(cfe.getNace());
        acroForm.getField("comp_nom").setValue(cfe.getNomComp());
        acroForm.getField("comp_add").setValue(cfe.getAddComp());
        acroForm.getField("comp_phone").setValue(cfe.getPhoneComp());
        acroForm.getField("comp_mail").setValue(cfe.getMailComp());

        // Handle the current date field if necessary
        // Assuming there is a field in the PDF form named 'date' for the current date
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        PDTextField dateField = (PDTextField) acroForm.getField("date");
        if (dateField != null) {
            dateField.setValue(currentDate);
        }
    }

}
