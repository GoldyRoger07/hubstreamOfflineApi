package com.hubstream.api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.*;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hubstream.api.model.Episode;
import com.hubstream.api.model.Saison;
import com.hubstream.api.model.StreamFile;
import com.hubstream.api.repository.EpisodeRepository;

@Service
public class EpisodeService {
    @Autowired
    EpisodeRepository episodeRepository;

    @Autowired
    ParametresFileService parametresFileService;

    @Autowired
    ConfigurationService configurationService;

    String cheminSerie = "";
    String cheminAnime = "";
    

    @SuppressWarnings("unchecked")
    public void initPath(){
        Map<String,Object> config = configurationService.getConfig();
        Map<String,Object> parametresFileConfig =(HashMap<String,Object>) config.get("parametresFile");
        cheminSerie = (String) parametresFileConfig.get("folderSeries");
        cheminAnime = (String) parametresFileConfig.get("folderAnimes");
       
    }


    public Optional<Episode> getEpisode(final int idEpisode) {
        return episodeRepository.findById(idEpisode);
    }

    public List<Episode> getEpisodes() {
        return episodeRepository.findAll();
    }

    public List<Episode> getEpisodes(Saison saison){
        return episodeRepository.findAllBySaison(saison);
    }

    public void deleteEpisode(final int idEpisode) {
        episodeRepository.deleteById(idEpisode);
    }

    public Episode save(Episode episode) {
        Episode episodeSaved = episodeRepository.save(episode);
        return episodeSaved;
    }

    
    public void saveEpisode(String name, Saison saison) {
        initPath();
                // cheminRacine =
        // parametresFileService.getParametresFiles().get(0).getFolderRacine();
        

        if (!exist(name, saison)) {
            Episode episode = new Episode();
            episode.setCodeEpisode(AdminService.generateRandomString(16));
            int numeroEpisode = Integer.parseInt(extractionChaineCaractere(name, "E", "."));
            episode.setTitre("episode " + numeroEpisode);
            episode.setNumero(numeroEpisode);

            StreamFile fichierVideo = new StreamFile();
            fichierVideo.setName(name);
            fichierVideo.setTypeFile("video");
            

            if (saison.getSerie() != null){
                fichierVideo.setTypeContenu("serie");
                fichierVideo
                        .setFilePath(saison.getSerie().getTitre() + "/" + saison.getTitre() + "/"
                                + name);
            }

            if (saison.getAnime() != null){
                fichierVideo.setTypeContenu("anime");
                fichierVideo
                        .setFilePath(saison.getAnime().getTitre() + "/" + saison.getTitre() + "/"
                                + name);
            }

            episode.setFichierVideo(fichierVideo);
            episode.setSaison(saison);

            save(episode);
        }
    }

    public boolean exist(String name, Saison saison) {
        for (Episode e : getEpisodes(saison)) {
            if (e.getFichierVideo().getName().equals(name))
                return true;
        }
        return false;
    }

    public String extractionChaineCaractere(String chaineOriginale, String pointA, String pointB) {

        String regex = Pattern.quote(pointA) + "(.*?)" + Pattern.quote(pointB);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(chaineOriginale);

        if (matcher.find()) {
            String partieExtraite = matcher.group(1);
            return partieExtraite;
        } else {
            System.out.println("Les points A et B ne sont pas présents dans la chaîne.");
        }

        return "non trouver";
    }

    public int getNumberFromString(String input) {
        Pattern pattern = Pattern.compile("\\d+");

        // Créez un objet Matcher pour rechercher la correspondance dans la chaîne
        // d'entrée
        Matcher matcher = pattern.matcher(input);

        // Vérifiez si une correspondance a été trouvée
        if (matcher.find()) {
            // Utilisez group(0) pour obtenir la correspondance complète
            String match = matcher.group(0);

            // Convertissez la correspondance en entier si nécessaire
            return Integer.parseInt(match);

        } else {
            System.out.println("Aucun chiffre trouvé dans la chaîne.");
        }
        return 0;
    }
}
