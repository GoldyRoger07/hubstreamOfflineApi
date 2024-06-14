package com.hubstream.api.model;

import jakarta.persistence.*;

import lombok.Data;

@Entity
@Data
public class ParametresFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idParametresFile;

    @Column(name = "folder_films")
    private String folderFilms;

    @Column(name = "folder_series")
    private String folderSeries;

    @Column(name = "folder_animes")
    private String folderAnimes;

    @Column(name = "folder_racine")
    private String folderRacine;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_folder_racine")
    private FolderRacine newfolderRacine;

    public ParametresFile() {
        folderFilms = "/films";
        folderSeries = "/series";
        folderAnimes = "/animes";
    }
}
