package com.hubstream.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "telechargements")
public class Telechargement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTelechargement;

    private String type;

    private double prix;
}
