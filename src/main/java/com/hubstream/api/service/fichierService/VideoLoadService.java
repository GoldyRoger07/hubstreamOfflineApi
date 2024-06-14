package com.hubstream.api.service.fichierService;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hubstream.api.model.StreamFile;
import com.hubstream.api.service.ParametresFileService;

@Service
public class VideoLoadService extends LoadService {

    @Autowired
    ParametresFileService parametresFileService;

    @Override
    public StreamFile uploadToFileDataSystem(MultipartFile file,String folder) throws IOException {
        
        return saveFile(file,folder); 
    }
    
}
