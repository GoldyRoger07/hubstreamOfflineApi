package com.hubstream.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hubstream.api.model.Film;


public interface FilmRepository extends JpaRepository<Film,Integer> {
    public Optional<Film> findByCode(String code);
    public Film findByTitre(String titre);
}
