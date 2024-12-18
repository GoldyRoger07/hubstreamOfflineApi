package com.hubstream.api.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.hubstream.api.model.Serie;
import com.hubstream.api.model.SeriesComparator;
import com.hubstream.api.model.StreamFile;
import com.hubstream.api.model.TestResponse;
import com.hubstream.api.service.ConfigurationService;
import com.hubstream.api.service.ParametresFileService;
import com.hubstream.api.service.ParametresIpServices;
import com.hubstream.api.service.SerieService;
import com.hubstream.api.service.StreamFileService;

@RestController
@RequestMapping("/api.hubstream.com")
public class SerieController {
    @Autowired
    SerieService serieService;

    @Autowired
    StreamFileService streamFileService;

    @Autowired
    ParametresFileService parametresFileService;

    @Autowired
    ParametresIpServices parametresIpServices;

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    RestTemplate restTemplate;

    String baseUrlApiOnline = "http://192.168.0.178:9001/api.online.hubstream.com";

    String cheminRacine = "";

    @SuppressWarnings("unchecked")
    public void initPath(){
        Map<String,Object> config = configurationService.getConfig();
        Map<String,Object> parametresFileConfig =(HashMap<String,Object>) config.get("parametresFile");
        
        cheminRacine = (String) parametresFileConfig.get("folderSeries");
    }

    @GetMapping("/series/aleatoire")
    public List<Serie> getAleatoireSeries() {
        List<Serie> series = serieService.getAleatoireSeries();
        Collections.sort(series, new SeriesComparator());

        return series;
    }

    @GetMapping("/series")
    public List<Serie> getSeries(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return serieService.getSeries(page, size);
    }

    @GetMapping("/series/all")
    public List<Serie> getAllSeries() {
        return serieService.getSeries();
    }

    @GetMapping("/serie/{id}")
    public Serie getSerie(@PathVariable("id") int id) {
        return serieService.getSerie(id).get();
    }

    @PutMapping("/serie")
    public String updateSerie(@RequestBody Serie serie) {
        serieService.save(serie);
        return "{\"status\":\"reussi\"}";
    }

    @DeleteMapping("/serie/{id}")
    public String deleteSerie(@PathVariable("id") int id) {
        serieService.deleteSerie(id);
        return "{\"status\":\"reussi\"}";
    }

    @SuppressWarnings("null")
    @GetMapping("/download/serie-episode/{videoName}/compte/{idCompte}")
    public ResponseEntity<FileSystemResource> downloadSerieEpisode(@PathVariable("videoName") String videoName,
            @PathVariable("idCompte") String idCompte) {
            initPath();
            StreamFile streamFile = streamFileService.getStreamFile(videoName).get();
            String chemin = "";
            String typeContenu = streamFile!=null?streamFile.getTypeContenu():"";
            if(typeContenu!=""){
                chemin = streamFileService.getCheminByTypeContenu(typeContenu);
            
                Path episodePath = Paths.get(chemin +"/"+
                streamFile.getFilePath());

                return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_OCTET_STREAM)
                            .body(new FileSystemResource(episodePath));

                

            }

        return null;
    }

    @GetMapping("/series/reload")
    @ResponseBody
    public List<Serie> getListeSeries() {
        
        return serieService.reloadSeries();
    }

    @GetMapping("/series/printScriptSql")
    public TestResponse generateSerieScriptSql(){
        TestResponse testResponse = new TestResponse();
        serieService.sauvegardeSerieSql();
        testResponse.setPass(true);
        return testResponse;
    }

}
