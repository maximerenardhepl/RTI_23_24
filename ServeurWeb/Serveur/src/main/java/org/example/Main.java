package org.example;

import com.sun.net.httpserver.HttpServer;
import org.example.Handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args)
    {
        HttpServer serveur = null;

        try{

            serveur = HttpServer.create(new InetSocketAddress(8080),0);
            serveur.createContext("/",new HandlerHtml());

            serveur.createContext("/style",new HandlerCss());
            serveur.createContext("/images",new HandlerImage());
            serveur.start();

            System.out.println("Démarrage serveur web");


        }catch (IOException e)
        {
            System.out.println("erreur de :" + e.getMessage());
        }
    }
}