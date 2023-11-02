package Modele;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;

public class Ovesp {
    private DataTransfer dataTransfer;

    private static final Ovesp ovesp = new Ovesp();
    public static Ovesp getInstance() { return ovesp; }

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
    }

    public void cancel() {

    }

    public void cancelAll() {

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
}
