package com.hubstream.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name="Saisons")
public class Saison{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSaison;

    private String titre;

    private int numero;

    @OneToMany(cascade = CascadeType.ALL,targetEntity = Episode.class,mappedBy = "saison")
    private List<Episode> episodes;

    @ManyToOne
    @JoinColumn(name="id_serie")
    @JsonIgnore
    private Serie serie;

    @ManyToOne
    @JoinColumn(name="id_anime")
    @JsonIgnore
    private Anime anime;

    public Episode getEpisodeByNum(int num){
            for(Episode e:episodes){
                if(e.getNumero()==num)
                    return e;
            }
        return null;
    }

}