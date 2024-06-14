package com.hubstream.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hubstream.api.model.FolderRacine;
import com.hubstream.api.repository.FolderRacineRepository;

@Service
public class FolderRacineService {
    @Autowired
    FolderRacineRepository folderRacineRepository;

    public Optional<FolderRacine> getFolderRacine(int id) {
        return folderRacineRepository.findById(id);
    }

    public List<FolderRacine> getFolderRacines() {
        return folderRacineRepository.findAll();
    }

    public void deleteAll() {
        folderRacineRepository.deleteAll();
    }

    public void deleteFolderRacine(int id) {
        folderRacineRepository.deleteById(id);
    }

    public FolderRacine save(FolderRacine folderRacine) {
        return folderRacineRepository.save(folderRacine);
    }

}
