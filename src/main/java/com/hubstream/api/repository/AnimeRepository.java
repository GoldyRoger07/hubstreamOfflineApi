package com.hubstream.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hubstream.api.model.Anime;

public interface AnimeRepository extends JpaRepository<Anime,Integer>{
    public Anime findByTitre(String titre);
}
