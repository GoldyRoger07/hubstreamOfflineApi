package com.hubstream.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hubstream.api.model.Telechargement;

public interface TelechargementRepository extends JpaRepository<Telechargement, Integer> {
    public Optional<Telechargement> findByType(String type);
}
