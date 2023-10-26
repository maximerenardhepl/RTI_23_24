package Modele;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Properties;

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

    }

    public void logout() {

    }

    public void cancel() {

    }

    public void cancelAll() {

    }
}
