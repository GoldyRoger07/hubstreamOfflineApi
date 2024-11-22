package com.hubstream.api.model;

import jakarta.persistence.*;

import lombok.Data;

@Entity
@Data
public class StreamFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_stream_file")
    private int idStreamFile;

    private String name;

    private String typeFile;

    private String typeContenu;
    
    @Column(name="file_path")
    private String filePath;



}
