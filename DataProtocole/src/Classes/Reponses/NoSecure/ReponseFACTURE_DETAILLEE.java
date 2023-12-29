package Classes.Reponses.NoSecure;

import Classes.Data.Article;
import Intefaces.Reponse;

import java.util.ArrayList;

public class ReponseFACTURE_DETAILLEE implements Reponse {
    private ArrayList<Article> listeArticles;

    public ReponseFACTURE_DETAILLEE(ArrayList<Article> listeArticles) {
        this.listeArticles = listeArticles;
    }

    public ArrayList<Article> getListeArticles() {
        return listeArticles;
    }
}
