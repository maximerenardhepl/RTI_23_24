package Modele;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Ovesp {
    private DataTransfer dataTransfer;

    private int NumArt = 1;
    private ArrayList<Article> panier;

    private Ovesp() {
        panier = new ArrayList<>();
    }

    private static final Ovesp ovesp = new Ovesp();

    public static Ovesp getInstance() { return ovesp; }
    ////////////////////////////////////////////////////////////////////////////////////
    public int getNumArt() {return NumArt;}
    public void setNumArt(int numArt) {NumArt = numArt;}
    ////////////////////////////////////////////////////////////////////////////////////
    private Article ArtCourant;
    public void setArtCourant(Article artCourant) {ArtCourant = artCourant;}
    public Article getArtCourant() {
        return ArtCourant;
    }
    ////////////////////////////////////////////////////////////////////////////////////
    public void init() {
        if(dataTransfer == null) {
            dataTransfer = new DataTransfer();
        }
    }

    public boolean login(String username, String password, boolean isNewClient) throws Exception {
        String requete;
        if(isNewClient) {
            requete = "REGISTER#" + username + "#" + password;
        }
        else {
            requete = "LOGIN#" + username + "#" + password;
        }

        String reponse = exchange(requete);
        System.out.println("Réponse reçue: " + reponse);

        String[] elementsReponse = reponse.split("#");
        if(elementsReponse[1].equals("ok")) {
            return true;
        }
        else {
            throw new Exception("Erreur de login!");
        }
    }

    public void logout() throws IOException {
        String requete = "LOGOUT";
        String reponse = exchange(requete);
        System.out.println("Réponse reçue: " + reponse);
    }

    public void cancel(int indiceArticleSelectionne) throws Exception {
        if(indiceArticleSelectionne > 0 && indiceArticleSelectionne < panier.size()) {
            Article artSelectionne = panier.get(indiceArticleSelectionne);

            String requete = "CANCEL#" + String.valueOf(artSelectionne.getId()) + "#" + String.valueOf(artSelectionne.getQuantite()) + "#" + String.valueOf(indiceArticleSelectionne);
            String reponse = exchange(requete);
            System.out.println("Réponse reçue: " + reponse);

            panier.remove(indiceArticleSelectionne);
        }
    }

    public void cancelAll() throws Exception {
        String requete = "CANCELALL";
        String reponse = exchange(requete);
        System.out.println("Réponse reçue: " + reponse);
        panier.clear();
    }

    public void closeConnection() {
        try {
            dataTransfer.getSocket().close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadProtocol() {
        if(dataTransfer.getSocket() != null) {
            closeConnection();
        }
        dataTransfer = new DataTransfer();
    }

    private String exchange(String requete) throws IOException {

        String reponse = "";
        System.out.println("Requete envoyée: " + requete);
        if(dataTransfer.send(requete) == -1) {
            closeConnection();
        }
        else {
            try {
                reponse = dataTransfer.receive();
            }
            catch(IOException e) {
                closeConnection();
                throw e;
            }

        }
        return reponse;
    }

    public void Consult(int idArticle) throws Exception {

        Article tempo;

        String requete = "CONSULT#" + idArticle;
        String reponse = exchange(requete);

        System.out.println("Réponse reçue : " + reponse);

        String[] elementsReponse = reponse.split("#");
        if (elementsReponse[1].equals("KO"))
        {
            int errCode = Integer.parseInt(elementsReponse[2]);
            String message;
        }
        else
        {
            int id = Integer.parseInt(elementsReponse[2]);
            String intitule = elementsReponse[3];
            int stock = Integer.parseInt(elementsReponse[4]);
            String image = elementsReponse[5];
            float prix = Float.parseFloat(elementsReponse[6]);

            tempo = new Article(id, intitule, stock, image, prix);

            //article dans le singleton donc accessible partout
            ArtCourant = tempo;
        }
    }

    //pas test juste fait a la rache
    public Article Achat(int idArticle, int quantite) throws Exception {
        String requete = "ACHAT#" + idArticle + "#" + quantite;
        String reponse = exchange(requete);

        System.out.println("Réponse reçue: " + reponse);

        String[] elementsReponse = reponse.split("#");
        String token = elementsReponse[1];

        Article resArticle;

        if (token.equals("KO"))
        {
            int errCode = Integer.parseInt(elementsReponse[2]);
            String message;
        }
        else
        {
            int id = Integer.parseInt(elementsReponse[2]);
            String intitule = elementsReponse[3];
            int stock = Integer.parseInt(elementsReponse[4]);
            String image = elementsReponse[5];
            float prix = Float.parseFloat(elementsReponse[6]);

            resArticle = new Article(id, intitule, stock, image, prix);
            return resArticle;
        }
        return null;
    }


}
