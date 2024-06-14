package com.hubstream.api.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hubstream.api.model.ParametresFile;
import com.hubstream.api.repository.ParametresFileRepository;

@Service
public class ParametresFileService {

    @Autowired
    ParametresFileRepository parametresFileRepository;

    public Optional<ParametresFile> getParametresFile(int id) {
        return parametresFileRepository.findById(id);
    }

    public List<ParametresFile> getParametresFiles() {
        return parametresFileRepository.findAll();
    }

    public void deleteAll() {
        parametresFileRepository.deleteAll();
    }

    public ParametresFile save(ParametresFile parametresFile) {
        return parametresFileRepository.save(parametresFile);
    }

    public List<String> getFilesName(String chemin) {
        List<String> listeFilesName = new ArrayList<>();

        try {

            Path directory = Paths.get(chemin);

            if (Files.exists(directory)) {

                try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
                    for (Path subDirector : stream) {
                        listeFilesName.add(subDirector.getFileName().toString());

                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return listeFilesName;
    }

    public String getFileName(List<String> liste, String indice) {
        for (String s : liste) {
            if (s.contains(indice))
                return s;
        }
        return "aucun";
    }

    public String getFileNameByExtension(List<String> liste, List<String> extensions) {
        String finalName = "aucun";
        for (String ext : extensions) {
            if (finalName.equals("aucun"))
                finalName = getFileName(liste, ext);
        }

        return finalName;
    }

    public List<String> getFoldersName(String chemin) {
        List<String> listeFoldersName = new ArrayList<>();

        try {
            Path directory = Paths.get(chemin);

            if (Files.exists(directory) && Files.isDirectory(directory)) {
                DirectoryStream.Filter<Path> filter = entry -> Files.isDirectory(entry);
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, filter)) {
                    for (Path subDirector : stream) {
                        listeFoldersName.add(subDirector.getFileName().toString());

                    }
                }
            } else
                System.out.println("Le dossier specifie n'existe pas ou n'est pas un dossier.");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return listeFoldersName;
    }

    public void sauvegardeSql(String fileName, List<String> sqlQueries) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String sqlQuery : sqlQueries) {
                writer.write(sqlQuery);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUrlSigned(String url, String code) {

        return url + "?code=" + code;
    }

}
