package org.example;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;

public class HandlerRoot implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("requete reçue");

        String requestPath = exchange.getRequestURI().getPath();
        System.out.println("requestPath = " + requestPath);

        String requestMethod = exchange.getRequestMethod();
        System.out.println("requestMethod = " + requestMethod);

        Headers requestHeaders = exchange.getRequestHeaders();

        System.out.println("Header : " );

        for (String key : requestHeaders.keySet())
        {
            System.out.println(key + ": " + requestHeaders.get(key));
        }

        InputStream is = exchange.getRequestBody();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String ligne;
        System.out.println("Request Body :");

        while ((ligne = br.readLine()) != null)
        {
            System.out.println(ligne);
        }

        // Ecriture de la reponse
        String reponse = "ceci est le corps de la reponse";

        //on crée le header en disant que c'est du text
        exchange.getResponseHeaders().set("Content-Type","text/plain");

        //code plus dire au client la lg de la chaine
        exchange.sendResponseHeaders(200,reponse.length());

        //ouvre le flux envoi sous forme de tableau de byte au client
        OutputStream os = exchange.getResponseBody();
        os.write(reponse.getBytes());
        os.close();

        System.out.println("--- Reponse envoyee ---\n");
    }
}
