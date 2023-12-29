package org.example;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Handler;

public class Main {
    public static void main(String[] args)
    {
        HttpServer serveur = null;

        try{

            serveur = HttpServer.create(new InetSocketAddress(8080),0);
            serveur.createContext("/",new HandlerRoot());
            serveur.start();

            System.out.println("Démarrage serveur web");


        }catch (IOException e)
        {
            System.out.println("erreur de :" + e.getMessage());
        }
    }
}