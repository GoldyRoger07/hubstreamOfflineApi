package com.hubstream.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hubstream.api.model.FolderRacine;
import com.hubstream.api.model.ParametresFile;
import com.hubstream.api.service.FolderRacineService;
import com.hubstream.api.service.ParametresFileService;

@RestController
@RequestMapping("/api.hubstream.com")
public class ParametresFileController {

    @Autowired
    FolderRacineService folderRacineService;

    @Autowired
    ParametresFileService parametresFileService;

    @GetMapping("/folderRacines")
    public List<FolderRacine> getFolderRacines() {
        return folderRacineService.getFolderRacines();
    }

    @PostMapping("/folderRacine")
    public String addFolderRacine(@RequestBody FolderRacine folderRacine) {
        folderRacineService.save(folderRacine);
        return "{\"status\":\"reussi\"}";
    }

    @PutMapping("/folderRacine")
    public String updateFolderRacine(@RequestBody FolderRacine folderRacine) {
        folderRacineService.save(folderRacine);
        return "{\"status\":\"reussi\"}";
    }

    @GetMapping("/folderRacine/{id}")
    public FolderRacine getFolderRacine(@PathVariable int id) {
        return folderRacineService.getFolderRacine(id).get();
    }

    @DeleteMapping("/folderRacine/{id}")
    public void deleteFolderRacine(@PathVariable int id) {
        folderRacineService.deleteFolderRacine(id);
    }

    @GetMapping("/parametresFiles")
    public List<ParametresFile> getParametresFiles() {
        return parametresFileService.getParametresFiles();
    }

    @GetMapping("/parametresFile/{id}")
    public ParametresFile getParametresFile(@PathVariable("id") int id) {
        return parametresFileService.getParametresFile(id).get();
    }

    @PostMapping("/parametresFile")
    public void addParametresFile(@RequestBody ParametresFile parametresFile) {
        parametresFileService.save(parametresFile);
    }

    @PutMapping("/parametresFile")
    public void updateParametresFile(@RequestBody ParametresFile parametresFile) {
        parametresFileService.save(parametresFile);
    }

}
