package com.hubstream.api.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hubstream.api.model.Anime;
import com.hubstream.api.model.Saison;
import com.hubstream.api.model.Serie;


public interface SaisonRepository extends JpaRepository<Saison,Integer> {
    public List<Saison> findAllBySerie(Serie serie);
    public List<Saison> findAllByAnime(Anime anime);
}
