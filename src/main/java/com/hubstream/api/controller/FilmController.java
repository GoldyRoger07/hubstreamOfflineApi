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

import com.hubstream.api.model.Film;
import com.hubstream.api.model.FilmsComparator;
import com.hubstream.api.model.StreamFile;
import com.hubstream.api.model.TestResponse;
import com.hubstream.api.service.FilmService;
import com.hubstream.api.service.ParametresFileService;
import com.hubstream.api.service.ParametresIpServices;
import com.hubstream.api.service.StreamFileService;

@RestController
@RequestMapping("/api.hubstream.com")
public class FilmController {

    @Autowired
    FilmService filmService;

    @Autowired
    StreamFileService streamFileService;

    @Autowired
    ParametresFileService parametresFileService;

    @Autowired
    ParametresIpServices parametresIpServices;

    @Autowired
    RestTemplate restTemplate;

    String baseUrlApiOnline = "http://192.168.0.178:9001/api.online.hubstream.com";


    @GetMapping("/films/aleatoire")
    public List<Film> getAleatoireFilms() {
        List<Film> films = filmService.getAleatoireFilms();
        Collections.sort(films, new FilmsComparator());

        return films;
    }

    @GetMapping("/films")
    public List<Film> getFilms(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return filmService.getFilms(page, size);

    }

    @GetMapping("/films/all")
    public List<Film> getAllFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/film/{code}")
    public Film getFilm(@PathVariable("code") String code) {
        Film f = filmService.getFilm(code);
        return f;
    }

    @PutMapping("/film")
    public String updateFilm(@RequestBody Film film) {
        filmService.save(film);
        return "{\"status\":\"reussi\"}";
    }

    @DeleteMapping("/film/{id}")
    public String deleteFilm(@PathVariable("id") int id) {
        filmService.deleteFilm(id);
        return "{\"status\":\"reussi\"}";
    }

    // @GetMapping("/download/film/{videoName}/compte/{idCompte}")
    // public ResponseEntity<InputStreamResource>
    // downloadFilm(@PathVariable("videoName") String videoName,
    // @PathVariable("idCompte") String idCompte) {
    // Optional<Compte> compteOpt = compteService.getCompte(idCompte);
    // Compte compte = compteOpt.isPresent() ? compteOpt.get() : null;
    // StreamFile streamFile = streamFileService.getStreamFile(videoName).get();
    // if (compte != null) {
    // String cheminRacine =
    // parametresFileService.getParametresFiles().get(0).getFolderRacine();
    // if (activerPlanService.testPlanActive(compte.getActiverPlans(), "Film")) {
    // Path filmPath = Paths.get(cheminRacine +
    // streamFile.getFilePath());

    // try {
    // FileInputStream fileInputStream = new FileInputStream(filmPath.toFile());
    // InputStreamResource resource = new InputStreamResource(fileInputStream);

    // HttpHeaders headers = new HttpHeaders();
    // headers.setContentDispositionFormData("attachment",
    // streamFile.getFilePath());
    // headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

    // return ResponseEntity
    // .status(HttpStatus.PARTIAL_CONTENT)
    // .headers(headers)
    // .body(resource);
    // } catch (IOException e) {
    // e.printStackTrace();
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    // }

    // }

    // }

    // return null;
    // }

    @GetMapping("/download/film/{videoName}/compte/{idCompte}")
    public ResponseEntity<FileSystemResource> downloadFilm(@PathVariable("videoName") String videoName,
            @PathVariable("idCompte") String idCompte) {
            baseUrlApiOnline = parametresIpServices.getParamIps().get(0).getBaseUrlApiOnline();
            StreamFile streamFile = streamFileService.getStreamFile(videoName).get();
            String cheminRacine = parametresFileService.getParametresFiles().get(0).getFolderRacine();
            String url = baseUrlApiOnline+"/activerPlans/test/compte/"+idCompte+"/Film";
            ResponseEntity<TestResponse> responseEntity = restTemplate.getForEntity(url, TestResponse.class);
            TestResponse testResponse = responseEntity.getBody();
            if (testResponse!=null && testResponse.isPass()){
                  Path filmPath = Paths.get(cheminRacine +
                        streamFile.getFilePath());

                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(new FileSystemResource(filmPath));

            }

         return null;
    }

    @GetMapping("/films/reload")
    @ResponseBody
    public List<Film> getListeFilms() {
        filmService.updateFilms();
        return filmService.getFilms();
    }


    @GetMapping("/films/printScriptSql")
    public TestResponse generateFilmScriptSql(){
        TestResponse testResponse = new TestResponse();
        filmService.sauvegardeFilmSql();
        testResponse.setPass(true);
        return testResponse;
    }

}
