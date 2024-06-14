package com.hubstream.api.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hubstream.api.model.Anime;
import com.hubstream.api.model.Episode;
import com.hubstream.api.model.Saison;
import com.hubstream.api.model.Serie;
import com.hubstream.api.repository.SaisonRepository;

@Service
public class SaisonService {
   
    @Autowired
    private SaisonRepository saisonRepository;

    @Autowired
    private EpisodeService episodeService;

    @Autowired
    ParametresFileService parametresFileService;


    public Optional<Saison> getSaison(final int idSaison){
        return saisonRepository.findById(idSaison);
    }

    public List<Saison> getSaisons(){
        return saisonRepository.findAll();
    }

    public void deleteSaison(final int idSaison){
        saisonRepository.deleteById(idSaison);
    }

    public Saison save(Saison saison){
        Saison saisonSaved = saisonRepository.save(saison);
        return saisonSaved;
    }

    public void saveSaison(String name,Serie serie,Anime anime){
       
        if(serie!=null){
            if(!exist(name,serie,null)){
                Saison saison = new Saison();
                saison.setTitre(name);
                saison.setNumero(getNumberFromString(name));
                saison.setSerie(serie);   
                save(saison);
            }
        }

        if(anime!=null){
            if(!exist(name,null,anime)){
                Saison saison = new Saison();
                saison.setTitre(name);
                 saison.setNumero(getNumberFromString(name));
                saison.setAnime(anime);  
                save(saison);
            }
        }

    }

    public int getNumberFromString(String input){
        Pattern pattern = Pattern.compile("\\d+");

        // Créez un objet Matcher pour rechercher la correspondance dans la chaîne d'entrée
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

    public boolean exist(String name,Serie serie,Anime anime){
        
        if(serie!=null){
            for(Saison s: serie.getSaisons()){
                if(s.getTitre().equals(name))
                return true;
            }
        }

        if(anime!=null){   
            for(Saison s: anime.getSaisons()){
                if(s.getTitre().equals(name))
                     return true;
                                  
            }
        }
          
            return false;
    }

    public void deleteAllEpisodeFromSaison(Saison saison){
        List<Episode> episodes = saison.getEpisodes();
        for(Episode e:episodes){
            episodeService.deleteEpisode(e.getIdEpisode());
        }
    }

    public List<Episode> getEpisodesByAlphabOrder(Saison resultSaison) {
        List<Episode> episodes= resultSaison.getEpisodes();

        List<Episode> finaleListe = new ArrayList<>();

       List<Integer> numeroEpisodes = new ArrayList<>();

       for(Episode e:episodes){
           numeroEpisodes.add(e.getNumero());
       }

       Collections.sort(numeroEpisodes);

           for(Integer numero: numeroEpisodes){
               for(Episode episode:episodes){
                   if(numero == episode.getNumero()){
                       finaleListe.add(episode);
                   }
               }
           }
       
           return finaleListe;
    }

    

}
