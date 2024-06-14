package com.hubstream.api.service;

import java.io.IOException;

import java.util.List;
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
        String cheminRacine = parametresFileService.getParametresFiles().get(0).getFolderRacine();
        return downloadService.downloadFromFileDataSystem(cheminRacine + streamFile.get().getFilePath());
    }
}
