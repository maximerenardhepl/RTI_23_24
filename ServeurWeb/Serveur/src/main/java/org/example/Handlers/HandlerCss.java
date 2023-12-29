package org.example.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class HandlerCss implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String requestPath = exchange.getRequestURI().getPath();
        String requestMethod = exchange.getRequestMethod();

        System.out.print("HandlerHtml (methode" + requestMethod +") = " + requestPath + " --> ");

        if(requestPath.endsWith(".css"))
        {
            String temp = System.getProperty("user.dir") + "\\src\\main\\resources";

            String fichier = "\\style\\style.css";
            File file = new File(temp + fichier);

            if(file.exists())
            {
                exchange.sendResponseHeaders(200, file.length());
                exchange.getResponseHeaders().set("Content-Type", "text/css");
                OutputStream os = exchange.getResponseBody();
                Files.copy(file.toPath(),os);
                os.close();
                System.out.println("OK");
            }
            else
            {
                Erreur404(exchange);
            }
        }
        else
        {
            Erreur404(exchange);
        }
    }

    private void Erreur404(HttpExchange exchange) throws IOException
    {
        String reponse = "Fichier HTML introuvable !!!";
        exchange.sendResponseHeaders(404, reponse.length());
        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        OutputStream os = exchange.getResponseBody();
        os.write(reponse.getBytes());
        os.close();
        System.out.println("KO");
    }
}
