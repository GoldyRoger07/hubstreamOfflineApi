package com.hubstream.api.service;

import java.util.ArrayList;
import java.util.List;
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
        String chemin = parametresFileService.getParametresFiles().get(0).getFolderFilms();
        String cheminRacine = parametresFileService.getParametresFiles().get(0).getFolderRacine();

        if (!isExist(name)) {
            Film film = new Film();
            StreamFile imageCover = new StreamFile();
            StreamFile fichierVideo = new StreamFile();

            List<String> liste = parametresFileService.getFilesName(cheminRacine + chemin + "/" + name);
            List<String> extensionImages = new ArrayList<>();

            extensionImages.add("jpg");
            extensionImages.add("jpeg");
            extensionImages.add("png");
            extensionImages.add("webp");

            List<String> extensionVideos = new ArrayList<>();
            extensionVideos.add("mp4");
            extensionVideos.add("webm");

            String imageName = parametresFileService.getFileNameByExtension(liste, extensionImages);
            String fichierVideoName = parametresFileService.getFileNameByExtension(liste, extensionVideos);

            imageCover.setName(imageName);
            imageCover.setType("image");

            imageCover.setFilePath(chemin + "/" + name + "/" + imageName);

            fichierVideo.setName(fichierVideoName);
            fichierVideo.setType("video");
            fichierVideo.setFilePath(chemin + "/" + name + "/" + fichierVideoName);

            film.setTitre(name);
            film.setImageCover(imageCover);
            film.setFichierVideo(fichierVideo);
            film.setDescriptionFilm("description");
            film.setAnnee(2000);
            film.setGenre("Genre");
            film.setPays("Pays");
            film.setRealisateur("Realisateur");
            film.setCast("Cast");
            film.setTemps("temps");
            film.setCode(AdminService.generateRandomString(16));
            save(film);
        }

    }

    public void updateFilms() {
        String chemin = parametresFileService.getParametresFiles().get(0).getFolderFilms();
        String cheminRacine = parametresFileService.getParametresFiles().get(0).getFolderRacine();
        System.out.println(cheminRacine+chemin);
        for (String name : parametresFileService.getFoldersName(cheminRacine + chemin)) {
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
        String chemin = parametresFileService.getParametresFiles().get(0).getFolderFilms();
        String cheminRacine = parametresFileService.getParametresFiles().get(0).getFolderRacine();

        for (String folderName : parametresFileService.getFoldersName(cheminRacine + chemin)) {
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

}
