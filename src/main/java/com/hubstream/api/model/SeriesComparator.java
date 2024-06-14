package com.hubstream.api.model;

import java.util.Comparator;

public class SeriesComparator implements Comparator<Serie> {
    @Override
    public int compare(Serie serie1, Serie serie2) {
        // Compare en ordre décroissant en fonction de l'attribut 'annee'
        return Integer.compare(serie2.getAnnee(), serie1.getAnnee());
    }
}
