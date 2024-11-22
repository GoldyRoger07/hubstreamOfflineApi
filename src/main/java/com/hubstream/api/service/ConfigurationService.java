package com.hubstream.api.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hubstream.api.model.Anime;
import com.hubstream.api.model.FileHandler;
import com.hubstream.api.model.Film;
import com.hubstream.api.model.Serie;

@Service
public class ConfigurationService {

    public void startConfig(){
        FileHandler.createFile("C:\\Hubstream","hubstream.config.json");
        createJsonFiles();
        FileHandler.createFolder("C:\\Hubstream\\Films");
        FileHandler.createFolder("C:\\Hubstream\\Series");
        FileHandler.createFolder("C:\\Hubstream\\Animes");
        FileHandler.createFolder("C:\\Hubstream\\Temp");
        FileHandler.createFolder("C:\\Hubstream\\Temp\\Films");
        FileHandler.createFolder("C:\\Hubstream\\Temp\\Series");
        FileHandler.createFolder("C:\\Hubstream\\Temp\\Animes");
        createDefaultConfiguration();
    }

    public void createJsonFiles(){
        File filmsJsonFile = FileHandler.createFile("C:\\Hubstream","films.json");
        
        Map<String,Object> filmJson = new HashMap<>();
        Map<String,Object> serieJson = new HashMap<>();
        Map<String,Object> animeJson = new HashMap<>();
        
        filmJson.put("films",new ArrayList<>());
        serieJson.put("series",new ArrayList<>());
        animeJson.put("animes",new ArrayList<>());


        if(FileHandler.isFileEmpty(filmsJsonFile.getAbsolutePath()))
            FileHandler.writeJsonToFile("C:\\Hubstream","films.json",filmJson);
        
        File seriesJsonFile = FileHandler.createFile("C:\\Hubstream","series.json");
        
        if(FileHandler.isFileEmpty(seriesJsonFile.getAbsolutePath()))
            FileHandler.writeJsonToFile("C:\\Hubstream","series.json",serieJson);

        File animesJsonFile = FileHandler.createFile("C:\\Hubstream","animes.json");
        
        if(FileHandler.isFileEmpty(animesJsonFile.getAbsolutePath()))
            FileHandler.writeJsonToFile("C:\\Hubstream","animes.json",animeJson);
    }

    public void createDefaultConfiguration(){
        String fileName = "C:\\Hubstream\\hubstream.config.json";

        if(FileHandler.isFileEmpty(fileName)){
            Map<String,Object> config = new HashMap<>();
            Map<String,Object> parametresFile = new HashMap<>();
            Map<String,Object> films = new HashMap<>();
            Map<String,Object> series = new HashMap<>();
            Map<String,Object> animes = new HashMap<>();
            Map<String,Object> task = new HashMap<>();
            Map<String,Object> host = new HashMap<>();

            parametresFile.put("isUpdated", false);
            parametresFile.put("folderRacine", "C:/Hubstream");
            parametresFile.put("folderFilms", "C:/Hubstream/Films");
            parametresFile.put("folderSeries", "C:/Hubstream/Series");
            parametresFile.put("folderAnimes", "C:/Hubstream/Animes");

            films.put("isUpdated",false);
            films.put("titres",new ArrayList<>());
            films.put("path","films.json");

            series.put("isUpdated",false);
            series.put("titres",new ArrayList<>());
            series.put("path","series.json");

            animes.put("isUpdated",false);
            animes.put("titres",new ArrayList<>());
            animes.put("path","animes.json");

            task.put("interval",600000);
            task.put("delay",0);

            host.put("hubstreamOnlineApi","http://192.168.0.178:9001/api.online.hubstream.com");
            host.put("hubstreamOfflineApi","http://192.168.0.178:9002/api.offline.hubstream.com");
            host.put("hubstreamVideoServer","http://192.168.0.178:8080");

            config.put("appName","Hubstream");
            config.put("hasUpdate", false);
            config.put("parametresFile",parametresFile);
            config.put("films",films);
            config.put("series",series);
            config.put("animes",animes);
            config.put("task",task);
            config.put("host", host);

            setConfig(config);
        }
    }

    public void setConfig(Map<String,Object> config){
        writeJsonToFile("C:\\Hubstream", "hubstream.config.json", config);
    }

    public void writeJsonToFile(String folderPath,String fileName,Object object){
        FileHandler.writeJsonToFile(folderPath, fileName, object);
    }

    @SuppressWarnings("unchecked")
    public Map<String,Object> getConfig(){
       return (Map<String,Object>) FileHandler.getObjectFromJsonFile("C:\\Hubstream\\hubstream.config.json");
    }

    
    @SuppressWarnings("unchecked")
    public List<Serie> getSeriesFromJson(){ 
         
        Map<String,Object> serieJson = (HashMap<String,Object>) FileHandler.getObjectFromJsonFile("C:\\Hubstream\\series.json");  
        List<Serie> series =  FileHandler.seriesJson(serieJson); 
        return series;
    } 

    
    @SuppressWarnings("unchecked")
    public List<Film> getFilmsFromJson(){
        Map<String,Object> filmJson = (HashMap<String,Object>) FileHandler.getObjectFromJsonFile("C:\\Hubstream\\films.json");  
        List<Film> films =  FileHandler.filmsJson(filmJson); 
        return films; 
    } 

    @SuppressWarnings("unchecked")
    public List<Anime> getAnimesFromJson(){
        Map<String,Object> animeJson = (HashMap<String,Object>) FileHandler.getObjectFromJsonFile("C:\\Hubstream\\animes.json");  
        List<Anime> animes =  FileHandler.animesJson(animeJson);
        return animes;
    } 

    public void setFilmsToJson(List<Film> films){
        Map<String,Object> filmJson = new HashMap<>();

        filmJson.put("films", films);

        writeJsonToFile("C:\\Hubstream", "films.json", filmJson);
    }

    public void setSeriesToJson(List<Serie> series){
        Map<String,Object> serieJson = new HashMap<>();

        serieJson.put("series", series);

        writeJsonToFile("C:\\Hubstream", "series.json", serieJson);
    }


    public void setAnimesToJson(List<Anime> animes){
        Map<String,Object> animeJson = new HashMap<>();

        animeJson.put("animes", animes);

        writeJsonToFile("C:\\Hubstream", "animes.json", animeJson);
    }


    @SuppressWarnings("unchecked")
    public void renameFolderByContenu(String actualName,String newName,String typeContenu){
        Map<String,Object> config = getConfig();
        Map<String,Object> parametresFileConfig =(HashMap<String,Object>) config.get("parametresFile");
        String chemin = "";

        switch(typeContenu){
            case "film":
                chemin = (String) parametresFileConfig.get("folderFilms");
            break;
            case "serie":
                chemin = (String) parametresFileConfig.get("folderSeries");
            break;
            case "anime":
                chemin = (String) parametresFileConfig.get("folderAnimes");
            break;
        }

        FileHandler.renameFolder(chemin+"/"+actualName, chemin+"/"+newName);

    }

    public List<Film> getFilmsByTitres(Map<String,Object> filmsConfig){
        

        List<String> titres = FileHandler.getListFromJson(filmsConfig.get("titres"), new TypeReference<List<String>>(){});
        List<Film> films = getFilmsFromJson();
        List<Film> filmsByTitres = new ArrayList<>();

        titres.forEach(titre->{
            films.forEach(f->{
                if(titre.equals(f.getTitre()))
                    filmsByTitres.add(f);
            });
        });

        Map<String,Object> config = getConfig();
        
        filmsConfig.put("titres",new ArrayList<>());
       
        config.put("films",filmsConfig);

        setConfig(config);
        
        return filmsByTitres;
    }

    public List<Serie> getSeriesByTitres(Map<String,Object> seriesConfig){

        List<String> titres = FileHandler.getListFromJson(seriesConfig.get("titres"), new TypeReference<List<String>>(){});
        List<Serie> series = getSeriesFromJson();
        List<Serie> seriesByTitres = new ArrayList<>();

        titres.forEach(titre->{
            series.forEach(s->{
                if(titre.equals(s.getTitre()))
                    seriesByTitres.add(s);
            });
        });
        
        Map<String,Object> config = getConfig();
        
        seriesConfig.put("titres",new ArrayList<>());
       
        config.put("series",seriesConfig);

        setConfig(config);
       
        
        return seriesByTitres;
    }

    public List<Anime> getAnimesByTitres(Map<String,Object> animesConfig){

        List<String> titres = FileHandler.getListFromJson(animesConfig.get("titres"), new TypeReference<List<String>>(){});
        List<Anime> animes = getAnimesFromJson();
        List<Anime> animesByTitres = new ArrayList<>();

        titres.forEach(titre->{
            animes.forEach(a->{
                if(titre.equals(a.getTitre()))
                    animesByTitres.add(a);
            });
        });
        
        Map<String,Object> config = getConfig();
        
        animesConfig.put("titres",new ArrayList<>());
       
        config.put("animes",animesConfig);

        setConfig(config);
       
        
        return animesByTitres;
    }



}
