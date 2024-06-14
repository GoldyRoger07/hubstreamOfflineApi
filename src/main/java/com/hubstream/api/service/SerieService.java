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

import com.hubstream.api.model.Serie;
import com.hubstream.api.model.Saison;
import com.hubstream.api.model.StreamFile;
import com.hubstream.api.repository.SerieRepository;

@Service
public class SerieService {

    @Autowired
    SerieRepository serieRepository;

    @Autowired
    MainService<Serie> mainService;

    @Autowired
    ParametresFileService parametresFileService;

    @Autowired
    SaisonService saisonService;

    @Autowired
    EpisodeService episodeService;

    String chemin = "";
    String cheminRacine = "";

    public Optional<Serie> getSerie(final int idSerie) {
        return serieRepository.findById(idSerie);
    }

    public List<Serie> getSeries() {
        return getOnlyPresentSeries(serieRepository.findAll());
    }

    public List<Serie> getSeries(int page,int size){
        Pageable pageable = PageRequest.of(page,size);
        Page<Serie> seriePage = serieRepository.findAll(pageable);

        return getOnlyPresentSeries(seriePage.getContent());
    }

    public void deleteSerie(final int idSerie) {
        serieRepository.deleteById(idSerie);
    }

    public Serie save(Serie serie) {
        Serie serieSaved = serieRepository.save(serie);
        return serieSaved;
    }

    public List<Serie> getOnlyPresentSeries(List<Serie> series){
        List<Serie> onlyPresentSeries = new ArrayList<>();
        for (Serie serie : series) {
            if(isExistInLocal(serie.getTitre()))
                onlyPresentSeries.add(serie);
        }
        return onlyPresentSeries;
    }

    public boolean isExistInLocal(String name){
        String chemin = parametresFileService.getParametresFiles().get(0).getFolderSeries();
        String cheminRacine = parametresFileService.getParametresFiles().get(0).getFolderRacine();

        for (String folderName : parametresFileService.getFoldersName(cheminRacine + chemin)) {
           if(folderName.equals(name))
                return true;
        }

        return false;
    }

    public Serie saveSerie(String name) {
        chemin = parametresFileService.getParametresFiles().get(0).getFolderSeries();
        cheminRacine = parametresFileService.getParametresFiles().get(0).getFolderRacine();

        if (!exist(name)) {
            Serie serie = new Serie();
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

            serie.setImageCover(imageCover);

            serie.setTitre(name);
            serie.setAnnee(2000);
            serie.setGenre("Genre");
            serie.setPays("Pays");
            serie.setCast("Cast");
            serie.setDescriptionSerie("Description");
            serie.setRealisateur("Realisateur");
            serie.setTemps("Temps");
            return save(serie);
        }
        return null;
    }

    public void updateSeries() {
        chemin = parametresFileService.getParametresFiles().get(0).getFolderSeries();
        cheminRacine = parametresFileService.getParametresFiles().get(0).getFolderRacine();
        for (String name : parametresFileService.getFoldersName(cheminRacine + chemin)) {
            saveSerie(name);
        }
    }

    public void updateSaisonInSeries() {
        chemin = parametresFileService.getParametresFiles().get(0).getFolderSeries();
        cheminRacine = parametresFileService.getParametresFiles().get(0).getFolderRacine();
        for (Serie s : getSeries()) {
            for (String n : parametresFileService.getFoldersName(cheminRacine + chemin + "/" + s.getTitre())) {
                saisonService.saveSaison(n, s, null);
            }
        }
    }

    public void updateEpisodeInSeries() {
        chemin = parametresFileService.getParametresFiles().get(0).getFolderSeries();
        cheminRacine = parametresFileService.getParametresFiles().get(0).getFolderRacine();

        for (Serie s : getSeries()) {
            for (Saison sa : s.getSaisons()) {
                for (String n : parametresFileService
                        .getFilesName(cheminRacine + chemin + "/" + s.getTitre() + "/" + sa.getTitre())) {
                    episodeService.saveEpisode(n, sa);
                }
            }
        }
    }

    public boolean exist(String name) {
        for (Serie s : getSeries()) {
            if (s.getTitre().equals(name))
                return true;
        }
        return false;
    }

    public List<Serie> getAleatoireSeries() {
        return getOnlyPresentSeries(mainService.getListAleatoire(getSeries()));
    }

    public List<Saison> getSaisonsByAlphabOrder(Serie serie) {
        List<Saison> saisons = serie.getSaisons();

        List<Saison> finaleListe = new ArrayList<>();

        List<Integer> numeroSaisons = new ArrayList<>();

        for (Saison s : saisons) {
            numeroSaisons.add(s.getNumero());
        }

        Collections.sort(numeroSaisons);

        for (Integer numero : numeroSaisons) {
            for (Saison saison : saisons) {
                if (numero == saison.getNumero()) {
                    finaleListe.add(saison);
                }
            }
        }

        return finaleListe;

    }

    public void sauvegardeSerieSql() {
        List<Serie> series = getSeries();

        List<String> sqlQueries = new ArrayList<>();

        for (Serie s : series) {
            String sqlQuerie = "update series set description_serie='" + s.getDescriptionSerie().replace("'", "\\'")
                    + "'," +
                    "genre='" + s.getGenre().replace("'", "\\'") + "', pays='" + s.getPays().replace("'", "\\'") + "',realisateur='" + s.getRealisateur().replace("'", "\\'") + "',"
                    +
                    "temps='" + s.getTemps() + "', cast='" + s.getCast().replace("'", "\\'") + "', annee=" + s.getAnnee() + " where titre='"
                    + s.getTitre() + "';";

            sqlQueries.add(sqlQuerie);
        }

        parametresFileService.sauvegardeSql("series.sql", sqlQueries);

    }

}
