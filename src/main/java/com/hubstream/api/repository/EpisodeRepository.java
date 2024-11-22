package com.hubstream.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hubstream.api.model.Episode;
import com.hubstream.api.model.Saison;

public interface EpisodeRepository extends JpaRepository<Episode,Integer>{
      public List<Episode> findAllBySaison(Saison saison);
}
