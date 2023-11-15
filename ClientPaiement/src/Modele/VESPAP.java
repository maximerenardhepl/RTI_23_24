package Modele;

import Classes.*;

import java.io.IOException;
import java.net.Socket;

public class VESPAP {
    Communication communication;
    private static VESPAP instance;

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
            ReponseLOGIN reponse = (ReponseLOGIN) communication.traiteRequete(requete);
            if(reponse.isValide()) {
                return true;
            }
            else {
                String msg = "Le nom d'utilisateur ou le mot passe entré est incorrect!";
                throw new Exception(msg);
            }
        }
        catch(IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void Logout() {

    }

    public void GetFactures() {

    }

    public void PayFacture() {

    }
}
