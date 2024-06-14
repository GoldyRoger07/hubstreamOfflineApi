package com.hubstream.api.service.fichierService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.stereotype.Service;

@Service
public class DownloadService {
    public byte[] downloadFromFileDataSystem(String filePath) throws IOException {

        byte[] file = Files.readAllBytes(new File(filePath).toPath());
        return file;
    }

}
