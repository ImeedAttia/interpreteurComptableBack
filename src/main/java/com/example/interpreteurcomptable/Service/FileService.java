package com.example.interpreteurcomptable.Service;

import com.example.interpreteurcomptable.Entities.FileEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    FileEntity storeFile(MultipartFile file, long userId);
    FileEntity getFile(long userId);
}
