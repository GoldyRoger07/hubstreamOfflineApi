package com.hubstream.api.model;

import jakarta.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "films")
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idFilm;

    private String code;

    private String titre;

    @Column(columnDefinition = "TEXT")
    private String descriptionFilm;

    private String pays;

    private String temps;

    private int annee;

    private String genre;

    private String cast;

    private String realisateur;

    @OneToOne(cascade = CascadeType.ALL)
    private StreamFile imageCover;

    @OneToOne(cascade = CascadeType.ALL)
    private StreamFile fichierVideo;

}