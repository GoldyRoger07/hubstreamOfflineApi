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

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    StreamFileService streamFileService;

    String chemin = "";
    


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
        initPath();
        for (String folderName : parametresFileService.getFoldersName( chemin)) {
           if(folderName.equals(name))
                return true;
        }

        return false;
    }



    public Anime saveAnime(String name) {
        initPath();
        if (!exist(name)) {
            Anime anime = new Anime();
            StreamFile imageCover = new StreamFile();

            String imageName = parametresFileService.getImageNameFromFile(chemin, name);

            imageCover.setName(imageName);
            imageCover.setTypeFile("image");
            imageCover.setTypeContenu("anime");
            imageCover.setFilePath( name + "/" + imageName);

            anime.setImageCover(imageCover);

            anime.setTitre(name);
            anime.setAnnee(2000);
            anime.setGenre("Genre");
            anime.setPays("Pays");
            anime.setDescriptionAnime("Description");
            anime.setAuteur("auteur");
            anime.setTemps("Temps");

            List<Anime> animes = configurationService.getAnimesFromJson();
            
            anime = save(anime);

            animes.add(anime);

            configurationService.setAnimesToJson(animes);

            return anime;
        }
        return null;

    }

    public void updateAnimes() {
        initPath();
        for (String name : parametresFileService.getFoldersName( chemin)) {
            saveAnime(name);
        }

        updateSaisonInAnimes();
    }

    public void updateSaisonInAnimes() {
        initPath();
        for (Anime a : getAnimes()) {
            for (String n : parametresFileService.getFoldersName( chemin + "/" + a.getTitre())) {
                saisonService.saveSaison(n, null, a);
            }
        }

        updateEpisodeInAnimes();
    }

    public void updateEpisodeInAnimes() {
        initPath();
        for (Anime a : getAnimes()) {
            for (Saison sa : saisonService.getSaisons(a)) {
                for (String n : parametresFileService
                        .getFilesName(chemin + "/" + a.getTitre() + "/" + sa.getTitre())) {
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

    @SuppressWarnings("unchecked")
    public void updateInfos(){
        Map<String,Object> config = configurationService.getConfig();
        Map<String,Object> animesConfig = (Map<String,Object>)config.get("animes");
        List<Anime> animes = new ArrayList<>();
        boolean isUpdated = (boolean) animesConfig.get("isUpdated");
        List<Anime> animesByTitres = configurationService.getAnimesByTitres(animesConfig);
        if(isUpdated){
            if(animesByTitres.size()>0)
                animes = animesByTitres;
            else
                animes = configurationService.getAnimesFromJson();

            animes.forEach(anime->{
                Anime a = getAnime(anime.getIdAnime()).get();

                if(a!=null){
                    if(!a.getTitre().equals(anime.getTitre())){
                        configurationService.renameFolderByContenu(a.getTitre(), anime.getTitre(), "anime");
                        a.setTitre(anime.getTitre());
                    }
                    a.setAnnee(anime.getAnnee());
                    a.setAuteur(anime.getAuteur());
                    a.setDescriptionAnime(anime.getDescriptionAnime());
                    a.setGenre(anime.getGenre());
                    a.setPays(anime.getPays());
                    a.setTemps(anime.getTemps());
                   
                    String imageName = parametresFileService.getImageNameFromFile(chemin, a.getTitre());
                                        
                    StreamFile  imageCover = streamFileService.getStreamFileUpdated(
                        a.getTitre(),imageName, "image", "anime", a.getImageCover());

                    a.setImageCover(imageCover);

                    save(a);

                    animesConfig.put("isUpdated",false);

                    config.put("animes",animesConfig);

                    configurationService.setConfig(config);
                    
                }
            });
        }

    }

    @SuppressWarnings("unchecked")
    public void initPath(){
        Map<String,Object> config = configurationService.getConfig();
        Map<String,Object> parametresFileConfig =(HashMap<String,Object>) config.get("parametresFile");
        chemin = (String) parametresFileConfig.get("folderAnimes");
    }

}
