package Modele;

import java.io.*;

public class Ovesp {
    private String requete;
    private String reponse;
    private DataTransfer dataTransfer;

    private static Ovesp ovesp = new Ovesp();
    public static Ovesp getInstance() { return ovesp; }

    private Ovesp() {
        dataTransfer = new DataTransfer();
    }

    public void login() {
        dataTransfer.send("bonjour");
    }

    public void logout() {

    }

    public void cancel() {

    }

    public void cancelAll() {

    }

    public void closeConnection() throws IOException {
        dataTransfer.getSocket().close();
    }

    private String exchange(String requete) {
        return "";
    }
}
