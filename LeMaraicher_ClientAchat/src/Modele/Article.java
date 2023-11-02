package Modele;

public class Article
{
    private int id;
    private String intitule;
    private int quantite;
    private String image;
    private float prix;

    public Article() {
        this(0, "default", 0, "default", 0.0f);
    }

    public Article(int id, String intitule, int quantite, String image, float prix) {
        setId(id);
        setIntitule(intitule);
        setQuantite(quantite);
        setImage(image);
        setPrix(prix);
    }

    public Article(Article art) {
        this(art.getId(), art.getIntitule(), art.getQuantite(), art.getImage(), art.getPrix());
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

    public void setImage(String image) {
        this.image = image;
    }

    public void setPrix(float prix) {
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

    public String getImage() {
        return image;
    }

    public float getPrix() {
        return prix;
    }
}
