package com.hubstream.api.repository;


import org.springframework.data.jpa.repository.JpaRepository;

 //import org.springframework.stereotype.Repository;

import com.hubstream.api.model.Saison;


public interface SaisonRepository extends JpaRepository<Saison,Integer> {
    
}
