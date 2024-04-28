package com.example.interpreteurcomptable.Controller;

import com.example.interpreteurcomptable.Entities.CFE;
import com.example.interpreteurcomptable.Entities.CVAE;
import com.example.interpreteurcomptable.Entities.FileEntity;
import com.example.interpreteurcomptable.Entities.Historique;
import com.example.interpreteurcomptable.Repository.HistoriqueRepository;
import com.example.interpreteurcomptable.Service.CVAEService;
import com.example.interpreteurcomptable.Service.FileService;
import com.example.interpreteurcomptable.Service.UserService;
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
@RequestMapping("/cvae")
@RequiredArgsConstructor
public class CvaeController {
    private final CVAEService cvaeService;
    private final HistoriqueRepository historiqueRepository;
    private final FileService fileService;

    @PostMapping
    public CVAE createCvae(@RequestBody CVAE cvae) {
        return cvaeService.addCVAE(cvae);
    }

    // complete functions
    @GetMapping(path = "/{id}")
    public CVAE getCVAEById(@PathVariable long id) {
        return cvaeService.getCVAEById(id);
    }

    @GetMapping
    public List<CVAE> getCVAEList() {
        return cvaeService.getCVAE();
    }

    @DeleteMapping("/{id}")
    public void deleteCVA(@PathVariable long id) {
        cvaeService.deleteCVAE(id);
    }

    @PutMapping("/{id}")
    public CVAE updateCVA(@PathVariable long id, @RequestBody CVAE cvae) {
        return cvaeService.updateCVAE(id, cvae);
    }


    @PostMapping("/fill-pdf/{cvaeId}/{userId}")
    public ResponseEntity<byte[]> fillPdfAndDownload(@RequestParam("file") MultipartFile file, @PathVariable long cvaeId,@PathVariable long userId) {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
            if (acroForm == null) {
                return ResponseEntity.badRequest().body("The provided PDF file does not contain a form.".getBytes());
            }

            // Fetch the CVAE data
            CVAE cvae = cvaeService.getCVAEById(cvaeId);
            if (cvae == null) {
                return ResponseEntity.notFound().build();
            }

            fillFields(acroForm, cvae);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream);
            byte[] pdfBytes = byteArrayOutputStream.toByteArray();
            MultipartFile multipartFile = new MockMultipartFile("file", "CVAE.pdf", "application/pdf", pdfBytes);
            // get current Date
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            CVAE cvae1 = cvaeService.addCVAE(cvae);
            FileEntity feInput = fileService.storeFilePdf(file, userId);
            FileEntity feOutput = fileService.storeFilePdf(multipartFile, userId);
            Historique historique = new Historique();
            historique.setCvae(cvae1);
            historique.setCreatedAt(now);
            historique.setUpdatedAt(now);
            historique.setTitre("CVAE - "+ dateFormat.format(now));
            historique.setInputFile(feInput);
            historique.setOutputFile(feOutput);
            historiqueRepository.save(historique);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=modifiedForm.pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(("Error processing PDF file: " + e.getMessage()).getBytes());
        }
    }

    private void fillFields(PDAcroForm acroForm, CVAE cvae) throws IOException {
        acroForm.getField("den").setValue(cvae.getDenomination() != null ? cvae.getDenomination() : "Default Denomination");
        acroForm.getField("add").setValue(cvae.getAddress() != null ? cvae.getAddress() : "Default Address");
        acroForm.getField("Code postal").setValue(cvae.getCodPost() != null ? cvae.getCodPost() : "Default CodPost");
        acroForm.getField("ville").setValue(cvae.getVille() != null ? cvae.getVille() : "Default Ville");
        acroForm.getField("siret").setValue(cvae.getSiret() != null ? cvae.getSiret() : "Default SIRET");
        // Setting static defaults for demonstration
        String ref = cvae.getRef();

        // Set values for fields 1 and 2
        if (ref.length() >= 8) {
            acroForm.getField("1").setValue(ref.substring(0, 2));
            acroForm.getField("2").setValue(ref.substring(2, 4));
            acroForm.getField("3").setValue(ref.substring(4, 6));
            acroForm.getField("4").setValue(ref.substring(6, 8));
            acroForm.getField("5").setValue(ref.substring(8, 10));
            acroForm.getField("6").setValue(ref.substring(10, 12));
        }

        // Set values for fields 7, 8, and 9
            String datePart = cvae.getDate().toString();
            acroForm.getField("7").setValue(datePart.substring(8, 10));
            acroForm.getField("8").setValue(datePart.substring(5, 7));
            acroForm.getField("9").setValue(datePart.substring(2,4));

        acroForm.getField("A2").setValue(String.valueOf(cvae.getValAjoute()));
        acroForm.getField("A3").setValue(String.valueOf(cvae.getRefAhiffaffair()));
        acroForm.getField("B6").setValue(String.valueOf(cvae.getRefChiffaffairGrp()));

        acroForm.getField("A").setValue(cvae.getRef() != null ? cvae.getRef() : "Default Reference");

        // Set the date field to the current system date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String currentDate = LocalDate.now().format(dtf);
        PDTextField dateField = (PDTextField) acroForm.getField("date");
        if (dateField != null) {
            dateField.setValue(currentDate);
        } else {
            System.out.println("Date field not found in the PDF.");
        }
    }
}
