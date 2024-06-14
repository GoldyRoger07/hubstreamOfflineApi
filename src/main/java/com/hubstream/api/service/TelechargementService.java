package com.hubstream.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hubstream.api.model.Telechargement;
import com.hubstream.api.repository.TelechargementRepository;

@Service
public class TelechargementService {

    @Autowired
    private TelechargementRepository telechargementRepository;

    public Optional<Telechargement> getTelechargement(final int idTelechargement) {
        return telechargementRepository.findById(idTelechargement);
    }

    public Optional<Telechargement> getTelechargementByType(String type) {
        return telechargementRepository.findByType(type);
    }

    public List<Telechargement> getTelechargements() {
        return telechargementRepository.findAll();
    }

    public void deleteTelechargement(final int idTelechargement) {
        telechargementRepository.deleteById(idTelechargement);
    }

    public Telechargement save(Telechargement telechargement) {
        Telechargement telechargementSaved = telechargementRepository.save(telechargement);
        return telechargementSaved;
    }
}
