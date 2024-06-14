package com.hubstream.api.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.hubstream.api.model.Serie;

public interface SerieRepository extends JpaRepository<Serie,Integer> {
    
}
