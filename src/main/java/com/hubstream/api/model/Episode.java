package com.hubstream.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name="episodes")
public class Episode {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int idEpisode;

  private String codeEpisode;

  private String titre;

  private int numero;
  
  @OneToOne(cascade = CascadeType.ALL)
  private StreamFile fichierVideo;

  @ManyToOne
  @JoinColumn(name="id_saison")
  @JsonIgnore
  private Saison saison;

}
