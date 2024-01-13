package org.example;

import com.sun.net.httpserver.HttpServer;
import org.example.handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.SQLException;

import org.example.modele.ConnexionBd;

public class Main {
    public static void main(String[] args)
    {
        HttpServer serveur = null;
        ConnexionBd con = null;

        try{
            con = ConnexionBd.getInstance();

            serveur = HttpServer.create(new InetSocketAddress(8080),0);
            serveur.createContext("/",new HandlerHtml());
            serveur.createContext("/FormArticle", new HandlerForm(con));
            serveur.createContext("/js",new HandlerJs());
            serveur.createContext("/style",new HandlerCss());
            serveur.createContext("/images",new HandlerImage());
            serveur.start();

            System.out.println("Démarrage serveur web");

        }catch (IOException e)
        {
            System.out.println("erreur de :" + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}