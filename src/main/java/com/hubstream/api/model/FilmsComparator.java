package com.hubstream.api.model;

import java.util.Comparator;

public class FilmsComparator implements Comparator<Film> {

    @Override
    public int compare(Film film1, Film film2) {
       
        return Integer.compare(film2.getAnnee(), film1.getAnnee());
    }
    
}
