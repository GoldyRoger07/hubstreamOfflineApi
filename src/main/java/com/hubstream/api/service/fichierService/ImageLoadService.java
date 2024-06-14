package com.hubstream.api.service.fichierService;

import java.io.IOException;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import com.hubstream.api.model.StreamFile;


@Service
public class ImageLoadService extends LoadService{

    @Override
    public StreamFile uploadToFileDataSystem(MultipartFile file,String folder) throws IOException {
                    return saveFile(file,folder);
    }

}
