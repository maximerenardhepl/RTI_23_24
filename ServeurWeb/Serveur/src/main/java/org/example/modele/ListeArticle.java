package org.example.modele;
import java.util.ArrayList;

public class ListeArticle {
    // Instance unique de la classe
    private static ListeArticle instance;

    // Liste d'articles
    private ArrayList<Article> listeArt;

    // Constructeur privé pour empêcher l'instanciation directe
    private ListeArticle() {
        listeArt = new ArrayList<>();
    }

    // Méthode pour obtenir l'instance unique de la classe (singleton)
    public static ListeArticle getInstance() {
        if (instance == null) {
            instance = new ListeArticle();
        }
        return instance;
    }
}

