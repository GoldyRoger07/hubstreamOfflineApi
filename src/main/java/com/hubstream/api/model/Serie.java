package com.hubstream.api.model;

import java.util.List;

import jakarta.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name="series")
public class Serie {
   @Id
   @GeneratedValue(strategy= GenerationType.IDENTITY)
   private int idSerie;

   @Column(unique = true)
   private String titre;

   @Column(columnDefinition = "TEXT")
   private String descriptionSerie;

   private String pays;

   private String temps;

   private int annee;

   private String genre;

   private String cast;

   private String realisateur;

   @OneToOne(cascade = CascadeType.ALL)
   private StreamFile imageCover;

   @OneToMany(cascade = CascadeType.ALL,targetEntity = Saison.class,mappedBy = "serie")
   private List<Saison> saisons;

   
}