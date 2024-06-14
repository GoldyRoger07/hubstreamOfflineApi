package com.hubstream.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hubstream.api.model.ParametresIp;

@Repository
public interface ParametresIpRepository extends JpaRepository<ParametresIp,Integer>{
    
}
