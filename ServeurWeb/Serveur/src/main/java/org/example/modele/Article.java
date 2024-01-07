package org.example.modele;

import java.util.Objects;

public class Article {
    private int id;
    private String nom;
    private float prix;
    private int quantiter;
    private String image;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public int getQuantiter() {
        return quantiter;
    }

    public void setQuantiter(int quantiter) {
        this.quantiter = quantiter;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Article article = (Article) o;

        if (id != article.id) return false;
        if (Float.compare(article.prix, prix) != 0) return false;
        if (quantiter != article.quantiter) return false;
        if (!Objects.equals(nom, article.nom)) return false;
        return Objects.equals(image, article.image);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (nom != null ? nom.hashCode() : 0);
        result = 31 * result + (prix != +0.0f ? Float.floatToIntBits(prix) : 0);
        result = 31 * result + quantiter;
        result = 31 * result + (image != null ? image.hashCode() : 0);
        return result;
    }
}
