package com.hubstream.api.model;

import java.util.List;

import jakarta.persistence.*;

import lombok.Data;

@Data
@Entity
@jakarta.persistence.Table(name="animes")
public class Anime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAnime;
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String descriptionAnime;
    private String pays;
    private String temps;
    private int annee;
    private String genre;
    private String auteur;

    @OneToOne(cascade = CascadeType.ALL)
    private StreamFile imageCover;

    @OneToMany(cascade = CascadeType.ALL,targetEntity = Saison.class,mappedBy = "anime")
    private List<Saison> saisons;
}
