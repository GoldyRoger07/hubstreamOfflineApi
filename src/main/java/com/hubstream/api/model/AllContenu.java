package com.hubstream.api.model;

import java.util.List;

import lombok.Data;

@Data
public class AllContenu {

  private List<Film> films;
  private List<Serie> series;
  private List<Anime> animes;  
  
}
