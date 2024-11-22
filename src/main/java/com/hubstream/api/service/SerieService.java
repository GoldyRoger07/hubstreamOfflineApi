package com.hubstream.api.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    StreamFileService streamFileService;

    String chemin = "";
    

    @SuppressWarnings("unchecked")
    public void initPath(){
        Map<String,Object> config = configurationService.getConfig();
        Map<String,Object> parametresFileConfig =(HashMap<String,Object>) config.get("parametresFile");
        chemin = (String) parametresFileConfig.get("folderSeries");
        
    }

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
         initPath();
        for (String folderName : parametresFileService.getFoldersName(  chemin)) {
           
            if(folderName.equals(name))
                return true;
            
        }

        return false;
    }

    public Serie saveSerie(String name) {
        initPath();
        if (!exist(name)) {
            Serie serie = new Serie();
            StreamFile imageCover = new StreamFile();

            String imageName = parametresFileService.getImageNameFromFile( chemin,name);

            imageCover = streamFileService.getStreamFileUpdated(
                name,imageName, "image", "serie", imageCover);

            serie.setImageCover(imageCover);

            serie.setTitre(name);
            serie.setAnnee(2000);
            serie.setGenre("");
            serie.setPays("");
            serie.setCast("");
            serie.setDescriptionSerie("D");
            serie.setRealisateur("");
            serie.setTemps("");

            List<Serie> series = configurationService.getSeriesFromJson();
            
            serie = save(serie);
                
            serie.setSaisons(new ArrayList<>());
            series.add(serie);
                
            configurationService.setSeriesToJson(series);
            
            
            return serie;
        }
        return null;
    }

    
    public void updateSeries() {
        initPath();
        
        for (String name : parametresFileService.getFoldersName( chemin)) {
            
            saveSerie(name);
        }

        updateSaisonInSeries();
    }

   
    public void updateSaisonInSeries() {
        try {
            initPath();
            for (Serie s : getSeries()) {
                for (String n : parametresFileService.getFoldersName(  chemin + "/" + s.getTitre())) {
                    saisonService.saveSaison(n, s, null);
                }
            }
        } catch (Exception e) {
           e.printStackTrace();
        }

       updateEpisodeInSeries();
    }

    public void updateEpisodeInSeries() {
        try {
            initPath();
       
        for (Serie s : getSeries()) {
           
            for (Saison sa : saisonService.getSaisons(s)) {
               
                for (String n : parametresFileService
                        .getFilesName( chemin + "/" + s.getTitre() + "/" + sa.getTitre())) {
                            
                            episodeService.saveEpisode(n, sa);
                }
            }
        }
        } catch (Exception e) {
            e.printStackTrace();
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

    public List<Serie> reloadSeries(){
        this.updateSeries();
        return getSeries();
    }

    @SuppressWarnings("unchecked")
    public void updateInfos(){
        Map<String,Object> config = configurationService.getConfig();
        Map<String,Object> seriesConfig = (Map<String,Object>)config.get("series");
        List<Serie> series = new ArrayList<>();
        boolean isUpdated = (boolean) seriesConfig.get("isUpdated");
        List<Serie> seriesByTitres = configurationService.getSeriesByTitres(seriesConfig);
        if(isUpdated){
            if(seriesByTitres.size()>0)
                series = seriesByTitres;
            else
                series = configurationService.getSeriesFromJson();
           
            series.forEach(serie->{
                Serie s = getSerie(serie.getIdSerie()).get();

                if(s!=null){
                    if(!s.getTitre().equals(serie.getTitre())){
                        configurationService.renameFolderByContenu(s.getTitre(), serie.getTitre(), "serie");
                        s.setTitre(serie.getTitre());
                    }
                    s.setAnnee(serie.getAnnee());
                    s.setCast(serie.getCast());
                    s.setDescriptionSerie(serie.getDescriptionSerie());
                    s.setRealisateur(serie.getRealisateur());
                    s.setPays(serie.getPays());
                    s.setGenre(serie.getGenre());
                    s.setTemps(serie.getTemps());

                    String imageName = parametresFileService.getImageNameFromFile(chemin, s.getTitre());
                                        
                    StreamFile  imageCover = streamFileService.getStreamFileUpdated(
                        s.getTitre(),imageName, "image", "serie", s.getImageCover());
                    
                    s.setImageCover(imageCover);
                    
                    save(s);

                    seriesConfig.put("isUpdated", false);

                    config.put("series", seriesConfig);

                    configurationService.setConfig(config);
                }
            });
        }
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
