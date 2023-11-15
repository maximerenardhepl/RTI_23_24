package Modele;

import Classes.*;
import Intefaces.Reponse;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class VESPAP {
    Communication communication;
    private static VESPAP instance;
    private ArrayList<Facture> listeFacture;
    private UserInfo infoclient;

    public ArrayList<Facture> getListeFacture() {
        return listeFacture;
    }

    private VESPAP() {
        communication = new Communication();
        listeFacture = new ArrayList<>();
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
                    infoclient = new UserInfo(password, username); //On stocke les informations du client connecté dans l'objet Communication de VESPAP.
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

    public void Logout(String username) {
        try
        {
            RequeteLOGOUT requete = new RequeteLOGOUT(username);
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

    public void PayFacture() {

    }

    public String getInfoclient() {
        return infoclient.getUsername();
    }
}
