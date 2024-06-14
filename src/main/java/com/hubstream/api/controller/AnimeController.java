package com.hubstream.api.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

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

import com.hubstream.api.model.Anime;
import com.hubstream.api.model.AnimesComparator;
import com.hubstream.api.model.StreamFile;
import com.hubstream.api.model.TestResponse;
import com.hubstream.api.service.AnimeService;
import com.hubstream.api.service.ParametresFileService;
import com.hubstream.api.service.ParametresIpServices;
import com.hubstream.api.service.StreamFileService;

@RestController
@RequestMapping("/api.hubstream.com")
public class AnimeController {

    @Autowired
    AnimeService animeService;

    @Autowired
    StreamFileService streamFileService;

    @Autowired
    ParametresFileService parametresFileService;

    @Autowired
    ParametresIpServices parametresIpServices;

    @Autowired
    RestTemplate restTemplate;

    String baseUrlApiOnline = "http://192.168.0.178:9001/api.online.hubstream.com";


    @GetMapping("/animes/aleatoire")
    public List<Anime> getAleatoireAnimes() {
        List<Anime> animes = animeService.getAleatoireAnimes();
        Collections.sort(animes, new AnimesComparator());

        return animes;
    }

    @GetMapping("/animes")
    public List<Anime> getAnimes(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return animeService.getAnimes(page, size);
    }

    @GetMapping("/animes/all")
    public List<Anime> getAllAnimes() {
        return animeService.getAnimes();
    }

    @GetMapping("/anime/{id}")
    public Anime getAnime(@PathVariable("id") int id) {
        return animeService.getAnime(id).get();
    }

    @PutMapping("/anime")
    public String updateAnime(@RequestBody Anime anime) {
        animeService.save(anime);
        return "{\"status\":\"reussi\"}";
    }

    @DeleteMapping("/anime/{id}")
    public String deleteAnime(@PathVariable("id") int id) {
        animeService.deleteAnime(id);
        return "{\"status\":\"reussi\"}";
    }

    @GetMapping("/download/anime-episode/{videoName}/compte/{idCompte}")
    public ResponseEntity<FileSystemResource> downloadSerieEpisode(@PathVariable("videoName") String videoName,
            @PathVariable("idCompte") String idCompte) {

            baseUrlApiOnline = parametresIpServices.getParamIps().get(0).getBaseUrlApiOnline();
            
            StreamFile streamFile = streamFileService.getStreamFile(videoName).get();
            String cheminRacine = parametresFileService.getParametresFiles().get(0).getFolderRacine();
            
            String url = baseUrlApiOnline+"/activerPlans/test/compte/"+idCompte+"/Anime";
            ResponseEntity<TestResponse> responseEntity = restTemplate.getForEntity(url, TestResponse.class);
            TestResponse testResponse = responseEntity.getBody();
            
            if (testResponse!=null && testResponse.isPass()){
                Path episodePath = Paths.get(cheminRacine +
                        streamFile.getFilePath());

                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(new FileSystemResource(episodePath));

            }
        
        return null;

    }

    @GetMapping("/animes/reload")
    @ResponseBody
    public List<Anime> getListeAnimes() {
        animeService.updateAnimes();
        animeService.updateSaisonInAnimes();
        animeService.updateEpisodeInAnimes();

        return animeService.getAnimes();
    }

    @GetMapping("/animes/printScriptSql")
    public TestResponse generateAnimeScriptSql(){
        TestResponse testResponse = new TestResponse();
        animeService.sauvegardeAnimeSql();
        testResponse.setPass(true);
        return testResponse;
    }
}
