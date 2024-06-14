package com.hubstream.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class MainService<T> {

    public List<T> getListAleatoire(List<T> listeCourant) {

        int nombreTotalContenu = 6, size = listeCourant.size();
        List<T> listeContenuAleatoire = new ArrayList<>();

        if (size < nombreTotalContenu)
            nombreTotalContenu = size;

        for (Integer nombreAleatoire : getDiffAleatoireNombre(nombreTotalContenu, size)) {
            listeContenuAleatoire.add(listeCourant.get(nombreAleatoire));
        }

        return listeContenuAleatoire;
    }

    public List<Integer> getDiffAleatoireNombre(int limit, int nombreContenu) {
        Random r = new Random();
        List<Integer> liste = new ArrayList<>();
        int i = 0;
        while (i < limit) {
            int randomNombre = r.nextInt(nombreContenu);
            if (!liste.contains(randomNombre)) {
                liste.add(randomNombre);
                i++;
            }
        }

        return liste;
    }

    public static String getMois(int numero) {

        String mois = "";

        switch (numero) {
            case 1:
                mois = "de janvier";
                break;
            case 2:
                mois = "de fevrier";
                break;
            case 3:
                mois = "de mars";
                break;
            case 4:
                mois = "d'avril";
                break;
            case 5:
                mois = "de mai";
                break;
            case 6:
                mois = "de juin";
                break;
            case 7:
                mois = "de juillet";
                break;
            case 8:
                mois = "d'aout";
                break;
            case 9:
                mois = "de septembre";
                break;
            case 10:
                mois = "d'octobre";
                break;
            case 11:
                mois = "de novembre";
                break;
            case 12:
                mois = "de decembre";
                break;
        }

        return mois;

    }
}
