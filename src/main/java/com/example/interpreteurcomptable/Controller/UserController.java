package com.example.interpreteurcomptable.Controller;

import com.example.interpreteurcomptable.Entities.FileEntity;
import com.example.interpreteurcomptable.Entities.Response.UploadFileResponse;
import com.example.interpreteurcomptable.Entities.Response.UserRequest;
import com.example.interpreteurcomptable.Entities.Response.UserResponse;
import com.example.interpreteurcomptable.Entities.User;
import com.example.interpreteurcomptable.Service.EmailService;
import com.example.interpreteurcomptable.Service.FileService;
import com.example.interpreteurcomptable.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userClientService;
    private final FileService dbFileStorageService;
    private final EmailService emailService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUserClient() {
        return new ResponseEntity<List<UserResponse>>(userClientService.getUser(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable long id) {
        User user = userClientService.getUserById(id);
        UserResponse us = UserResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
        return new ResponseEntity<UserResponse>(us, HttpStatus.OK);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable long id, @RequestBody UserRequest user) {
        User u = new User();
        u.setFirstName(user.getFirstName());
        u.setLastName(user.getLastName());
        u.setEmail(user.getEmail());
        return new ResponseEntity<UserResponse>(userClientService.updateUser(id, u), HttpStatus.ACCEPTED);
    }

    @PostMapping("/uploadFile/{userId}")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file, @PathVariable long userId) {
        FileEntity dbFile = dbFileStorageService.storeFile(file, userId);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
                .path(String.valueOf(dbFile.getId())).toUriString();

        return new UploadFileResponse(dbFile.getFileName(), fileDownloadUri, file.getContentType(), file.getSize());
    }

    @GetMapping("/downloadFile/{userId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable long userId) {
        // Load file from database
        FileEntity dbFile = dbFileStorageService.getFile(userId);

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));
    }

    @PostMapping("/mail/{userId}")
    public ResponseEntity<String> sendMail(@PathVariable long userId) {
        User user = userClientService.getUserById(userId);
        emailService.sendEmailWithTemplate(user);
        return new ResponseEntity<String>("Mail sent", HttpStatus.OK);
    }

    //fffffffffffffffffffffffffffffffffffff
    @PostMapping("/fill-pdf")
    public ResponseEntity<byte[]> fillPdfAndDownload(@RequestParam("file") MultipartFile file) {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
            if (acroForm != null) {
                fillFields(acroForm);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                document.save(byteArrayOutputStream);
                byte[] pdfBytes = byteArrayOutputStream.toByteArray();

                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=modifiedForm.pdf");
                headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(pdfBytes);
            } else {
                return ResponseEntity.badRequest()
                        .body("The provided PDF file does not contain a form.".getBytes());
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(("Error processing PDF file: " + e.getMessage()).getBytes());
        }
    }

    private void fillFields(PDAcroForm acroForm) throws IOException {
        String[] fieldNames = {"den", "add", "Code postal", "ville", "siret", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A2", "A3", "B6"};
        String[] fieldValues = {"deno", "TNS", "4081", "Sousse", "00000000000000", "00", "00", "00", "00", "00", "00", "00", "00", "00", "0000", "0000", "0000"};

        for (int i = 0; i < fieldNames.length; i++) {
            PDTextField field = (PDTextField) acroForm.getField(fieldNames[i]);
            if (field != null) {
                field.setValue(fieldValues[i]);
            } else {
                System.out.println("Field " + fieldNames[i] + " not found.");
            }
        }
    }
}
