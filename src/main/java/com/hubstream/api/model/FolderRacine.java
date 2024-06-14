package com.hubstream.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "folder_racines")
public class FolderRacine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idFolderRacine;

    private String name;
}
