package com.example.interpreteurcomptable.Repository;

import com.example.interpreteurcomptable.Entities.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity,Long> {
    Optional<FileEntity> findByUserId(Long userId);
}
