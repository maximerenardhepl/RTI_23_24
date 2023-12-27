package Modele;

import Classes.*;
import Intefaces.Reponse;

import java.io.IOException;
import java.util.ArrayList;

public class VESPAP {
    Communication communication;
    private static VESPAP instance;
    private ArrayList<Facture> listeFacture;
    private UserInfo infoclient;

    public ArrayList<Facture> getListeFacture() {
        return listeFacture;
    }
    public boolean isClientConnected() {
        return infoclient.isConnected();
    }

    private VESPAP() {
        communication = new Communication();
        listeFacture = new ArrayList<>();
        infoclient = new UserInfo();
    }

    public static VESPAP getInstance() {
        if (instance == null) {
            instance = new VESPAP();
        }
        return instance;
    }

    public boolean Login(String username, String password) throws Exception {
        try {
            communication.init();
        }
        catch(IOException e) {
            throw new Exception("Une erreur est survenue lors de la tentative de connexion au serveur...Veuillez réessayer!");
        }

        RequeteLOGIN requete = new RequeteLOGIN(username, password);
        try {

            Reponse reponse = communication.traiteRequete(requete);
            if(reponse instanceof ReponseErreurServeur) {
                throw new Exception(((ReponseErreurServeur) reponse).getMessage());
            }
            else if(reponse instanceof ReponseLOGIN) {
                if(((ReponseLOGIN) reponse).isValide()) {
                    infoclient.setUsername(username);
                    infoclient.setPassword(password); //On stocke les informations du client connecté dans l'objet UserInfo de VESPAP.
                    infoclient.connect();
                    return true;
                }
                else { //La réponse login nous a retourné false
                    String msg = ((ReponseLOGIN) reponse).getMessage(); //On récupère donc le message d'erreur fourni.
                    throw new Exception(msg);
                }
            }
            else {
                throw new Exception("Une erreur inconnue est survenue...");
            }
        }
        catch(IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void Logout() {
        try
        {
            String username = infoclient.getUsername();
            RequeteLOGOUT requete = new RequeteLOGOUT(username);
            infoclient.disconnect(); //Faire passer le booleen "isConnected" à false dans l'objet UserInfo.
            communication.getWriter().writeObject(requete);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Facture> GetFactures(int numClient) throws Exception {
        RequeteGET_FACTURES requete = new RequeteGET_FACTURES(numClient);
        try {
            Reponse reponse = communication.traiteRequete(requete);
            if(reponse instanceof ReponseErreurServeur) {
                throw new Exception(((ReponseErreurServeur) reponse).getMessage());
            }
            else if(reponse instanceof ReponseGET_FACTURES) {
                if(((ReponseGET_FACTURES) reponse).getListeFactures().size() > 0) {
                    return listeFacture = ((ReponseGET_FACTURES) reponse).getListeFactures();
                }
                else {
                    throw new Exception("Aucune facture n'a été trouvé pour ce numéro de client!");
                }
            }
            else {
                throw new Exception("Une erreur inconnue est survenue...");
            }
        }
        catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean PayFacture(Facture facture, String nom, String visa) {

        RequetePAY_FACTURE requete = new RequetePAY_FACTURE(facture.getId(),nom,visa);

        try
        {
            Reponse reponse = communication.traiteRequete(requete);
            if(reponse instanceof ReponseErreurServeur) {
                throw new Exception(((ReponseErreurServeur) reponse).getMessage());
            }
            else if(reponse instanceof ReponsePAY_FACTURE)
            {
                if(((ReponsePAY_FACTURE) reponse).isValide())
                {
                    return true;
                }
                else
                {
                    throw new Exception(((ReponsePAY_FACTURE) reponse).getMessage());
                }
            }
            else {
                throw new Exception("Une erreur inconnue est survenue...");
            }
        }
        catch(IOException | ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public ArrayList<Article> GetFactureDetaillee(Facture facture) throws Exception {
        RequeteFACTURE_DETAILLEE requete = new RequeteFACTURE_DETAILLEE(facture.getId());
        try {
            Reponse reponse = communication.traiteRequete(requete);
            if(reponse instanceof ReponseErreurServeur) {
                throw new Exception(((ReponseErreurServeur) reponse).getMessage());
            }
            else if(reponse instanceof ReponseFACTURE_DETAILLEE) {
                if(((ReponseFACTURE_DETAILLEE) reponse).getListeArticles().size() > 0) {
                    return ((ReponseFACTURE_DETAILLEE) reponse).getListeArticles();
                }
                else {
                    throw new Exception("Une erreur est survenue! La facture detaillee n'a pas pu etre affichee...");
                }
            }
            else {
                throw new Exception("Une erreur inconnue est survenue...");
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
