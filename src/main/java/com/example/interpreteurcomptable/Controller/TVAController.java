package com.example.interpreteurcomptable.Controller;

import com.example.interpreteurcomptable.Entities.FileEntity;
import org.springframework.mock.web.MockMultipartFile;
import com.example.interpreteurcomptable.Entities.Historique;
import com.example.interpreteurcomptable.Entities.Response.UserResponse;
import com.example.interpreteurcomptable.Entities.TVA;
import com.example.interpreteurcomptable.Entities.User;
import com.example.interpreteurcomptable.Repository.HistoriqueRepository;
import com.example.interpreteurcomptable.Service.FileService;
import com.example.interpreteurcomptable.Service.TVAService;
import com.example.interpreteurcomptable.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/tva")
@RequiredArgsConstructor
public class TVAController {
    private final TVAService tvaService;
    private final HistoriqueRepository historiqueRepository;
    private final FileService fileService;

    @GetMapping
    public ResponseEntity<TVA> getTVA() {
        return new ResponseEntity<TVA>(tvaService.getTVA(), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<TVA> addTVA(@RequestBody TVA tva) {
        return new ResponseEntity<TVA>(tvaService.addTVA(tva), HttpStatus.CREATED);
    }

    @PostMapping("/fill-pdf/{userId}")
    public ResponseEntity<byte[]> fillPdfAndDownload(@RequestParam("file") MultipartFile file, @PathVariable long userId) {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
            if (acroForm == null) {
                return ResponseEntity.badRequest().body("The provided PDF file does not contain a form.".getBytes());
            }

            // Assume getTVA() is a valid method to fetch your TVA entity
            TVA tva = tvaService.getTVA();
            if (tva == null) {
                return ResponseEntity.notFound().build();
            }

            fillFields(acroForm, tva);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream);
            byte[] pdfBytes = byteArrayOutputStream.toByteArray();

            // Creating a MockMultipartFile from byte[]
            MultipartFile multipartFile = new MockMultipartFile("file", "TVA.pdf", "application/pdf", pdfBytes);
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            TVA tva1 = tvaService.addTVA(tva);
            FileEntity feInput = fileService.storeFilePdf(file, userId);
            FileEntity feOutput = fileService.storeFilePdf(multipartFile, userId);
            Historique historique = new Historique();
            historique.setTva(tva1);
            historique.setCreatedAt(tva1.getCreationDate());
            historique.setUpdatedAt(tva1.getCreationDate());
            historique.setTitre("TVA - "+ dateFormat.format(now));
            historique.setInputFile(feInput);
            historique.setOutputFile(feOutput);
            historiqueRepository.save(historique);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=modifiedForm.pdf");
            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(("Error processing PDF file: " + e.getMessage()).getBytes());
        }
    }

    private void fillFields(PDAcroForm acroForm, TVA tva) throws IOException {
        // Set fields based on Tva properties
        acroForm.getField("du").setValue(String.valueOf(tva.getDu()));
        acroForm.getField("au").setValue(String.valueOf(tva.getAu()));
        acroForm.getField("nom").setValue(tva.getNom());
        acroForm.getField("add").setValue(tva.getAddress());
        acroForm.getField("siret").setValue(String.format("%.0f", tva.getSiret()));
        acroForm.getField("date").setValue(String.valueOf(tva.getCreationDate()));
        acroForm.getField("phone").setValue(String.valueOf(tva.getPhone()));

        // New fields
        acroForm.getField("vente").setValue(String.format("%.2f", tva.getVente()));
        acroForm.getField("aOI").setValue(String.format("%.2f", tva.getAOI()));
        acroForm.getField("tvaBrute20").setValue(String.format("%.2f", tva.getTvaBrute20()));
        acroForm.getField("tvaBrute10").setValue(String.format("%.2f", tva.getTvaBrute10()));
        acroForm.getField("tvaBrute55").setValue(String.format("%.2f", tva.getTvaBrute55()));
        acroForm.getField("totTvaBruteDue").setValue(String.format("%.2f", tva.getTotTvaBruteDue()));
        acroForm.getField("aBService").setValue(String.format("%.2f", tva.getABService()));
        acroForm.getField("totTvaDed").setValue(String.format("%.2f", tva.getTotTvaDed()));
        acroForm.getField("totTvaDue").setValue(String.format("%.2f", tva.getTotTvaDue()));
        acroForm.getField("tvaNetDue").setValue(String.format("%.2f", tva.getTvaNetDue()));
        acroForm.getField("taxAss").setValue(String.format("%.2f", tva.getTaxAss()));
        acroForm.getField("totPayer").setValue(String.format("%.2f", tva.getTotPayer()));
    }
}
