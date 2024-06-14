package com.hubstream.api.model;

import java.util.Comparator;

public class AnimesComparator implements Comparator<Anime> {

    @Override
    public int compare(Anime anime1, Anime anime2) {
        return Integer.compare(anime2.getAnnee(), anime1.getAnnee());
    }
    
}
    