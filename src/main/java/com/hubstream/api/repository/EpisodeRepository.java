package com.hubstream.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hubstream.api.model.Episode;

public interface EpisodeRepository extends JpaRepository<Episode,Integer>{
      
}
