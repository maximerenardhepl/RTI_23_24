package Modele;

import Classes.*;
import Intefaces.Reponse;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

public class VESPAP {
    Communication communication;
    private static VESPAP instance;
    private UserInfo infoclient;
    private VESPAP() {
        communication = new Communication();
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
                    return true;
                }
                else { //La réponse login nous a retourné false
                    String msg = "Le nom d'utilisateur ou le mot passe entré est incorrect!";
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

    public void Logout(String username){
        try
        {
            RequeteLOGOUT requete = new RequeteLOGOUT(username);
            communication.traiteRequete(requete);
        }
        catch(IOException | ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }

    }

    public void GetFactures() {

    }

    public void PayFacture() {

    }

    public UserInfo getInfoclient() {
        return infoclient;
    }
}
