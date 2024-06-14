package com.hubstream.api.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hubstream.api.service.StreamFileService;

@RestController
@RequestMapping("/api.hubstream.com")
public class Controller {

    @Autowired
    StreamFileService streamFileService;

    @GetMapping("/img/{idFile}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable int idFile) throws IOException {
        byte[] imageData = streamFileService.downloadFileFromFileSystem(idFile);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/jpg"))
                .body(imageData);
    }
}
