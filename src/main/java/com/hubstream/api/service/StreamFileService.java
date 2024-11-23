package com.hubstream.api.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hubstream.api.model.StreamFile;
import com.hubstream.api.repository.StreamFileRepository;
import com.hubstream.api.service.fichierService.DownloadService;

@Service
public class StreamFileService {

    @Autowired
    StreamFileRepository streamFileRepository;

    @Autowired
    ParametresFileService parametresFileService;

    @Autowired
    ConfigurationService configurationService;

    String cheminRacine = "";

    public Optional<StreamFile> getStreamFile(int id) {
        return streamFileRepository.findById(id);
    }

    public Optional<StreamFile> getStreamFile(String fileName) {
        return streamFileRepository.findByName(fileName);
    }

    public Optional<StreamFile> getStreamFileByFilePath(String filePath) {
        return streamFileRepository.findByFilePath(filePath);
    }

    public boolean isStreamFileNameExiste(String fileName) {
        Optional<StreamFile> streamFile = getStreamFile(fileName);
        if (streamFile.isPresent())
            return streamFile.get().getName().equals(fileName);

        return false;
    }

    public List<StreamFile> getStreamFiles() {
        return streamFileRepository.findAll();
    }

    public StreamFile save(StreamFile streamFile) {
        return streamFileRepository.save(streamFile);
    }

    // public StreamFile uploadImageToFileDataSystem(MultipartFile file) throws
    // IOException{
    // LoadService loadService = new ImageLoadService();
    // String folder =
    // parametresFileService.getParametresFile(1).get().getFolderImages();
    // return loadService.uploadToFileDataSystem(file,folder);
    // }

    // public StreamFile uploadVideoToFileDataSystem(MultipartFile file) throws
    // IOException{
    // LoadService loadService = new VideoLoadService();
    // String folder =
    // parametresFileService.getParametresFile(1).get().getFolderVideos();
    // return loadService.uploadToFileDataSystem(file,folder);
    // }

    public byte[] downloadFileFromFileSystem(int idFile) throws IOException {
        Optional<StreamFile> streamFile = getStreamFile(idFile);
        DownloadService downloadService = new DownloadService();
        String chemin = "";
        String typeContenu = streamFile.get()!=null?streamFile.get().getTypeContenu():"";
        if(typeContenu!=""){
            chemin = getCheminByTypeContenu(typeContenu);
            return downloadService.downloadFromFileDataSystem(chemin +"/"+ streamFile.get().getFilePath());
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public String getCheminByTypeContenu(String typeContenu){
        Map<String,Object> config = configurationService.getConfig();
        Map<String,Object> parametresFileConfig =(HashMap<String,Object>) config.get("parametresFile");
        String chemin = "";

        switch (typeContenu) {
            case "film":
                chemin = (String) parametresFileConfig.get("folderFilms");
            break;
            case "serie":
                chemin = (String) parametresFileConfig.get("folderSeries");
            break;
            case "anime":
                chemin = (String) parametresFileConfig.get("folderAnimes");
            break;
        
            default:
            break;
        }
        
        return chemin;
    }

    public StreamFile getStreamFileUpdated(String name,String fileName,String typeFile,String typeContenu,StreamFile streamFile){
        streamFile.setName(fileName);
        streamFile.setTypeFile(typeFile);
        streamFile.setTypeContenu(typeContenu);
        streamFile.setFilePath(name + "/" + fileName);

        return streamFile;
    }

    public StreamFile updateFilePath(StreamFile streamFile,String path){
        
      String filePath = streamFile.getFilePath();
      String[] filePathSplited = filePath.split("/");
      filePathSplited[0] = path;
      String newFilePath = String.join("/", filePathSplited);
      
      streamFile.setFilePath(newFilePath);
      
      return streamFile;
    }
}
