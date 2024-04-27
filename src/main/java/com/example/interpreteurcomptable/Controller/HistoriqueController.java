package com.example.interpreteurcomptable.Controller;

import com.example.interpreteurcomptable.Entities.FileEntity;
import com.example.interpreteurcomptable.Entities.Historique;
import com.example.interpreteurcomptable.Repository.FileRepository;
import com.example.interpreteurcomptable.Repository.HistoriqueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/historique")
@RequiredArgsConstructor
public class HistoriqueController {
    private final HistoriqueRepository historiqueRepository;
    @GetMapping
    public List<Historique> getHistorique() {
        return historiqueRepository.findAll();
    }
    private final FileRepository fileRepository;  // Assume you have a JPA repository for FileEntity

    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long fileId) {
        FileEntity fileEntity = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found with id " + fileId));

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileEntity.getFileName() + "\"");
        header.add(HttpHeaders.CONTENT_TYPE, fileEntity.getFileType());

        return ResponseEntity.ok()
                .headers(header)
                .body(fileEntity.getData());
    }
}
