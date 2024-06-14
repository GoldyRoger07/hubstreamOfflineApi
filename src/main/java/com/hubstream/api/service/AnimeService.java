package com.hubstream.api.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hubstream.api.model.Anime;
import com.hubstream.api.model.Saison;
import com.hubstream.api.model.StreamFile;
import com.hubstream.api.repository.AnimeRepository;

@Service
public class AnimeService {
    @Autowired
    private AnimeRepository animeRepository;

    @Autowired
    MainService<Anime> mainService;

    @Autowired
    ParametresFileService parametresFileService;

    @Autowired
    SaisonService saisonService;

    @Autowired
    EpisodeService episodeService;

    String chemin = "";
    String cheminRacine = "";

    public Optional<Anime> getAnime(final int idAnime) {
        return animeRepository.findById(idAnime);
    }

    public List<Anime> getAnimes() {
        return getOnlyPresentAnimes(animeRepository.findAll());
    }

    public List<Anime> getAnimes(int page,int size){
        Pageable pageable = PageRequest.of(page,size);
        Page<Anime> animePage = animeRepository.findAll(pageable);

        return getOnlyPresentAnimes(animePage.getContent());
    }

    public void deleteAnime(final int idAnime) {
        animeRepository.deleteById(idAnime);
    }

    public Anime save(Anime anime) {
        Anime animeSaved = animeRepository.save(anime);
        return animeSaved;
    }
    
    public List<Anime> getOnlyPresentAnimes(List<Anime> animes){
        List<Anime> onlyPresentAnimes = new ArrayList<>();
        for (Anime anime : animes) {
            if(isExistInLocal(anime.getTitre()))
                onlyPresentAnimes.add(anime);
        }
        return onlyPresentAnimes;
    }

    public boolean isExistInLocal(String name){
        String chemin = parametresFileService.getParametresFiles().get(0).getFolderAnimes();
        String cheminRacine = parametresFileService.getParametresFiles().get(0).getFolderRacine();

        for (String folderName : parametresFileService.getFoldersName(cheminRacine + chemin)) {
           if(folderName.equals(name))
                return true;
        }

        return false;
    }



    public Anime saveAnime(String name) {
        chemin = parametresFileService.getParametresFiles().get(0).getFolderAnimes();
        cheminRacine = parametresFileService.getParametresFiles().get(0).getFolderRacine();

        if (!exist(name)) {
            Anime anime = new Anime();
            StreamFile imageCover = new StreamFile();

            List<String> liste = parametresFileService.getFilesName(cheminRacine + chemin + "/" + name);

            List<String> extensionImages = new ArrayList<>();

            extensionImages.add("jpg");
            extensionImages.add("jpeg");
            extensionImages.add("png");
            extensionImages.add("webp");

            String imageName = parametresFileService.getFileNameByExtension(liste, extensionImages);

            imageCover.setName(imageName);
            imageCover.setType("image");
            imageCover.setFilePath(chemin + "/" + name + "/" + imageName);

            anime.setImageCover(imageCover);

            anime.setTitre(name);
            anime.setAnnee(2000);
            anime.setGenre("Genre");
            anime.setPays("Pays");
            anime.setDescriptionAnime("Description");
            anime.setAuteur("auteur");
            anime.setTemps("Temps");
            return save(anime);
        }
        return null;

    }

    public void updateAnimes() {
        chemin = parametresFileService.getParametresFiles().get(0).getFolderAnimes();
        cheminRacine = parametresFileService.getParametresFiles().get(0).getFolderRacine();
        for (String name : parametresFileService.getFoldersName(cheminRacine + chemin)) {
            saveAnime(name);
        }
    }

    public void updateSaisonInAnimes() {
        chemin = parametresFileService.getParametresFiles().get(0).getFolderAnimes();
        cheminRacine = parametresFileService.getParametresFiles().get(0).getFolderRacine();

        for (Anime a : getAnimes()) {
            for (String n : parametresFileService.getFoldersName(cheminRacine + chemin + "/" + a.getTitre())) {
                saisonService.saveSaison(n, null, a);
            }
        }
    }

    public void updateEpisodeInAnimes() {
        chemin = parametresFileService.getParametresFiles().get(0).getFolderAnimes();
        cheminRacine = parametresFileService.getParametresFiles().get(0).getFolderRacine();

        for (Anime a : getAnimes()) {
            for (Saison sa : a.getSaisons()) {
                for (String n : parametresFileService
                        .getFilesName(cheminRacine + chemin + "/" + a.getTitre() + "/" + sa.getTitre())) {
                    episodeService.saveEpisode(n, sa);
                }
            }
        }
    }

    public boolean exist(String name) {
        for (Anime a : getAnimes()) {
            if (a.getTitre().equals(name))
                return true;
        }
        return false;
    }

    public List<Anime> getAleatoireAnimes() {
        return getOnlyPresentAnimes(mainService.getListAleatoire(getAnimes()));
    }

    public List<Saison> getSaisonsByAlphabOrder(Anime anime) {
        List<Saison> saisons = anime.getSaisons();

        List<Saison> finaleListe = new ArrayList<>();

        List<String> titreSaisons = new ArrayList<>();

        for (Saison s : saisons) {
            titreSaisons.add(s.getTitre());
        }

        Collections.sort(titreSaisons);

        for (String titre : titreSaisons) {
            for (Saison saison : saisons) {
                if (titre.equals(saison.getTitre())) {
                    finaleListe.add(saison);
                }
            }
        }

        return finaleListe;

    }

    public void sauvegardeAnimeSql() {
        List<Anime> animes = getAnimes();

        List<String> sqlQueries = new ArrayList<>();

        for (Anime a : animes) {
            String sqlQuerie = "update animes set description_anime='" + a.getDescriptionAnime().replace("'", "\\'")
                    + "'," +
                    "genre='" + a.getGenre().replace("'", "\\'") + "', pays='" + a.getPays().replace("'", "\\'") + "',auteur='" + a.getAuteur().replace("'", "\\'") + "'," +
                    "temps='" + a.getTemps() + "',annee=" + a.getAnnee() + " where titre='" + a.getTitre() + "';";

            sqlQueries.add(sqlQuerie);
        }

        parametresFileService.sauvegardeSql("animes.sql", sqlQueries);

    }

}
