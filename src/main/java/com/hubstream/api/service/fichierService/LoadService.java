package com.hubstream.api.service.fichierService;

import java.io.File;
import java.io.IOException;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hubstream.api.model.StreamFile;

@Service
public abstract class LoadService {
    
     public abstract StreamFile uploadToFileDataSystem(MultipartFile file,String folder) throws IOException ;
       
      public StreamFile saveFile(MultipartFile file,String folder) throws IllegalStateException, IOException{
        String filePath = folder + file.getOriginalFilename();
       StreamFile streamFile = new StreamFile();
        
       streamFile.setName(file.getOriginalFilename());
       streamFile.setType(file.getContentType());
       streamFile.setFilePath(filePath);

       file.transferTo(new File(filePath));
            return streamFile; 
    }

       
    
    
}
