package com.hubstream.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.hubstream.api.service.AnimeService;
import com.hubstream.api.service.ConfigurationService;
import com.hubstream.api.service.FilmService;
import com.hubstream.api.service.SerieService;

@Component
public class DataInitializer implements ApplicationRunner{
    
    ConfigurationService configurationService;

    SerieService serieService;

    AnimeService animeService;

    FilmService filmService;

    int delay = 0;
    int interval = 0;

    @Autowired
    public DataInitializer(ConfigurationService configurationService,SerieService serieService,AnimeService animeService,FilmService filmService){
            this.configurationService = configurationService;
            this.serieService = serieService;
            this.animeService = animeService;
            this.filmService = filmService;
            
        
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run(ApplicationArguments args) throws Exception {
        configurationService.startConfig();
        Timer timer = new Timer();

        Map<String,Object> config = configurationService.getConfig();
        Map<String,Object> task = (HashMap<String,Object>) config.get("task");

        if(interval == 0){
            interval = (Integer) task.get("interval");
            delay = (Integer) task.get("delay");
        } 

        timer.scheduleAtFixedRate(new TimerTask(){

            @Override
            public void run() {
                filmService.updateFilms();
                filmService.updateInfos();
                animeService.updateAnimes();
                animeService.updateInfos();
                serieService.reloadSeries();
                serieService.updateInfos();
             
            }
            
        },delay,interval);
        
    }
}
