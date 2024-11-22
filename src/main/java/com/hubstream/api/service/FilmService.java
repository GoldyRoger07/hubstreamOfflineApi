package com.hubstream.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hubstream.api.model.Film;

import com.hubstream.api.model.StreamFile;
import com.hubstream.api.repository.FilmRepository;

@Service
public class FilmService {
    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    MainService<Film> mainService;

    @Autowired
    ParametresFileService parametresFileService;

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    StreamFileService streamFileService;

    String chemin = "";

    @SuppressWarnings("unchecked")
    public void initPath(){
        Map<String,Object> config = configurationService.getConfig();
        Map<String,Object> parametresFileConfig =(HashMap<String,Object>) config.get("parametresFile");
        chemin = (String) parametresFileConfig.get("folderFilms");
    }

    public Optional<Film> getFilm(final int idFilm) {
        return filmRepository.findById(idFilm);
    }

    public Film getFilm(final String code) {

        for (Film f : getFilms()) {
            if (f.getCode().equals(code))
                return f;
        }
        return null;
    }

    public List<Film> getFilms(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Film> filmPage = filmRepository.findAll(pageable);
        return getOnlyPresentFilms(filmPage.getContent());
    }

    public List<Film> getFilms() {
        return getOnlyPresentFilms(filmRepository.findAll());
    }

    public void deleteFilm(final int idFilm) {
        filmRepository.deleteById(idFilm);
    }

    public Film save(Film film) {
        Film filmSaved = filmRepository.save(film);
        return filmSaved;
    }

    public List<Film> getAleatoireFilms() {
        return getOnlyPresentFilms(mainService.getListAleatoire(getFilms()));
    }

    public void saveFilm(String name) {
        initPath();
        if (!isExist(name)) {
            Film film = new Film();
            
            String imageName = parametresFileService.getImageNameFromFile(chemin, name);
            String fichierVideoName = parametresFileService.getVideoNameFromFile(chemin, name);
            StreamFile imageCover = new StreamFile();
            StreamFile fichierVideo = new StreamFile();
             
            imageCover = streamFileService.getStreamFileUpdated(
                name,imageName, "image", "film", imageCover);
            
            fichierVideo = streamFileService.getStreamFileUpdated(
                name,fichierVideoName, "video", "film", fichierVideo);

            film.setFichierVideo(fichierVideo);
            film.setImageCover(imageCover);

            film.setTitre(name);
            film.setImageCover(imageCover);
            film.setFichierVideo(fichierVideo);
            film.setDescriptionFilm("");
            film.setAnnee(2000);
            film.setGenre("");
            film.setPays("");
            film.setRealisateur("");
            film.setCast("");
            film.setTemps("");
            film.setCode(AdminService.generateRandomString(16));
            
            List<Film> films = configurationService.getFilmsFromJson();

            film = save(film);

            films.add(film);

            configurationService.setFilmsToJson(films);

        }

    }

    public void updateFilms() {
        initPath();
        for (String name : parametresFileService.getFoldersName(chemin)) {
            saveFilm(name);
        }
    }

    public List<Film> getOnlyPresentFilms(List<Film> films){
        List<Film> onlyPresentFilms = new ArrayList<>();
        for (Film film : films) {
            if(isExistInLocal(film.getTitre()))
                onlyPresentFilms.add(film);
        }
        return onlyPresentFilms;
    }

    public boolean isExistInLocal(String name){
        initPath();
        for (String folderName : parametresFileService.getFoldersName( chemin)) {
           if(folderName.equals(name))
                return true;
        }

        return false;
    }

    public boolean isExist(String name) {
        for (Film f : getFilms()) {
            if (f.getTitre().equals(name))
                return true;
        }
        return false;
    }

    

    public void sauvegardeFilmSql() {
        List<Film> films = getFilms();

        List<String> sqlQueries = new ArrayList<>();

        for (Film f : films) {
            String sqlQuerie = "update films set description_film='" + f.getDescriptionFilm().replace("'", "\\'") + "',"
                    +
                    "genre='" + f.getGenre().replace("'", "\\'") + "', pays='" + f.getPays().replace("'", "\\'") + "',realisateur='" + f.getRealisateur().replace("'", "\\'") + "',"
                    +
                    "temps='" + f.getTemps() + "', cast='" + f.getCast().replace("'", "\\'") + "', code='" + f.getCode() + "',annee="
                    + f.getAnnee() + " where titre='" + f.getTitre() + "';";

            sqlQueries.add(sqlQuerie);
        }

        parametresFileService.sauvegardeSql("films.sql", sqlQueries);

    }

    @SuppressWarnings("unchecked")
    public void updateInfos(){
        Map<String,Object> config = configurationService.getConfig();
        Map<String,Object> filmsConfig = (Map<String,Object>)config.get("films");
        List<Film> films = new ArrayList<>();
        boolean isUpdated = (boolean) filmsConfig.get("isUpdated");
       List<Film> filmsByTitres = configurationService.getFilmsByTitres(filmsConfig);
        if(isUpdated){
            if(filmsByTitres.size()>0)
                films = filmsByTitres;
            else
                films = configurationService.getFilmsFromJson();

            films.forEach(film->{
                Film f = getFilm(film.getIdFilm()).get();

                if(f!=null){
                    // f.setTitre(film.getTitre());
                    if(!f.getTitre().equals(film.getTitre())){
                        configurationService.renameFolderByContenu(f.getTitre(), film.getTitre(), "film");
                        f.setTitre(film.getTitre());
                    }

                    f.setAnnee(film.getAnnee());
                    f.setCast(film.getCast());
                    f.setCode(film.getCode());
                    f.setDescriptionFilm(film.getDescriptionFilm());
                    f.setGenre(film.getGenre());
                    f.setTemps(film.getTemps());
                    f.setPays(film.getPays());
                   
                    String imageName = parametresFileService.getImageNameFromFile(chemin, f.getTitre());
                    String fichierVideoName = parametresFileService.getVideoNameFromFile(chemin, f.getTitre());

                    StreamFile  imageCover = streamFileService.getStreamFileUpdated(
                        f.getTitre(),imageName, "image", "film", f.getImageCover());
                    
                    StreamFile fichierVideo = streamFileService.getStreamFileUpdated(
                        f.getTitre(),fichierVideoName, "video", "film", f.getFichierVideo());

                    f.setImageCover(imageCover);
                    f.setFichierVideo(fichierVideo);

                    save(f);

                    filmsConfig.put("isUpdated",false);

                    config.put("films",filmsConfig);

                    configurationService.setConfig(config);

                }
            });
        }

    }

   


}
