package com.hubstream.api.model;

import java.io.File;
import java.util.List;
import java.util.Map;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FileHandler {

    public static File createFolder(String folderPath){
        File folder = new File(folderPath);

        if(!folder.exists()){
            boolean folderCreated = folder.mkdir();

            if(!folderCreated){
                System.out.println("Echec de la creation du dossier");
                return null;
            }     
        }

        return folder;
    }
    
    public static File createFile(String folderPath,String fileName){

        File folder = createFolder(folderPath);

        File file = new File(folder,fileName);

        try{
            if(!file.exists()){
               boolean fileCreated =  file.createNewFile();

               if(!fileCreated)
                    return null;
              

            }
                 
        }catch(Exception e){
            e.printStackTrace();
         }

        return file;

    }


    public static List<Serie> seriesJson(Map<String,Object> serieJson){

        List<Serie> series = getListFromJson(serieJson.get("series"),new TypeReference<List<Serie>>(){});

        return series;
    }

    public static List<Film> filmsJson(Map<String,Object> filmJson){

        List<Film> films = getListFromJson(filmJson.get("films"),new TypeReference<List<Film>>(){});

        return films;
    }

    public static List<Anime> animesJson(Map<String,Object> animeJson){

        List<Anime> animes = getListFromJson(animeJson.get("animes"),new TypeReference<List<Anime>>(){});

        return animes;
    }

    public static <T> List<T> getListFromJson(Object object,TypeReference<List<T>> typeReference){
        ObjectMapper objectMapper = new ObjectMapper();

        List<T> list = objectMapper.convertValue(object,typeReference);

        return list;
    }




    public static void writeJsonToFile(String folderPath,String fileName,Object object){
            File file = createFile(folderPath, fileName);

            ObjectMapper objectMapper = new ObjectMapper();

            try {
                
                objectMapper.writerWithDefaultPrettyPrinter()
                .writeValue(file, object);

                
            } catch (Exception e) {
                System.out.println("Erreur lors de l'ecriture dans le fichier JSON");
            }
    }

    public static boolean isFileEmpty(String fileName){
        File file = new File(fileName);

        if(!file.isFile() || !file.exists()){
            System.out.println("Ce n'est pas un fichier valide ou il existe pas ");
            return false;
        }

        if(file.length() == 0)
            return true;
        

        return false;
    }

    public static Object getObjectFromJsonFile(String fileName){
        File jsonFile = new File(fileName);

        if(!jsonFile.exists()){
            System.out.println("Le fichier json n'existe pas: "+jsonFile.getAbsolutePath());
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();

        try{

           return objectMapper.readValue(jsonFile,Object.class);

        }catch(Exception e){
            return null;
        }
        
    }


    public static void renameFolder(String actualPath,String newPath){
        File actualFolder = new File(actualPath);

        File newFolder = new File(newPath);

        if(!actualFolder.renameTo(newFolder))
            System.out.println("Une erreur est survenu lors du renommage du dossier");
    }

}
