package com.hubstream.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hubstream.api.model.StreamFile;


public interface StreamFileRepository extends JpaRepository<StreamFile,Integer>{
    
    public Optional<StreamFile> findByName(String fileName);

    public Optional<StreamFile> findByFilePath(String filePath);
}
