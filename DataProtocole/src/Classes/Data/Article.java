package Classes.Data;

import java.io.Serializable;

public class Article implements Serializable {
    private int id;
    private String intitule;
    private int quantite;
    private float prix;

    public Article(int id, String intitule, int quantite, float prix) {
        this.id = id;
        this.intitule = intitule;
        this.quantite = quantite;
        this.prix = prix;
    }

    public int getId() {
        return id;
    }

    public String getIntitule() {
        return intitule;
    }

    public int getQuantite() {
        return quantite;
    }

    public float getPrix() {
        return prix;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }
}
