package org.example.handlers;

import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonArray;
import com.nimbusds.jose.shaded.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.modele.Article;
import org.example.modele.ConnexionBd;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HandlerForm implements HttpHandler {

    private final ConnexionBd con;

    public HandlerForm(ConnexionBd con) {
        this.con = con;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        System.out.print("call HandlerForm");
        if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            System.out.print("request POST");
            System.out.print("aaaaaaaaaaaaaaaa");

            // Traitement des requêtes POST
            // Récupérer les données du corps de la requête
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            String data = br.readLine();
            System.out.print(data);

            try {
                JSONObject json = new JSONObject(data);

                int id = json.getInt("id");
                float price = json.getFloat("price");
                int quantity = json.getInt("quantity");

                System.out.print("ID : " + id);
                System.out.print("Prix : " + price);
                System.out.print("Quantité : " + quantity);

                String updateQuery = "UPDATE articles SET prix = " + price + ", stock = " + quantity + " WHERE id = " + id + ";";

                // Exécution de la requête SQL
                System.out.print(updateQuery);
                con.executeUpdate(updateQuery);

            } catch (JSONException e) {
                // Gérer l'exception JSON ici
                System.err.println("Erreur lors de la conversion JSON : " + e.getMessage());
                exchange.sendResponseHeaders(400, 0);  // Code 400 pour une mauvaise requête du client
                OutputStream os = exchange.getResponseBody();
                os.close();
            } catch (SQLException e) {
                // Gérer l'exception SQL ici
                System.err.println("Erreur lors de l'exécution de la requête SQL : " + e.getMessage());
                exchange.sendResponseHeaders(500, 0);  // Code 500 pour une erreur interne du serveur
                OutputStream os = exchange.getResponseBody();
                os.close();
            }
        }

        else
        {
            //prend la requete qui arrive du client pour recupe les article
            if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                //on va dans la bd et on prend les infos
                System.out.print("request GET");

                try {
                    // Mettre une condition
                    ResultSet resultSet = con.executeQuery("SELECT * FROM articles");

                    // Construire une liste d'articles à partir des résultats de la requête
                    ArrayList<Article> articlesList = new ArrayList<>();
                    while (resultSet.next()) {
                        Article article = new Article();
                        article.setId(resultSet.getInt("id"));
                        article.setNom(resultSet.getString("intitule"));
                        article.setPrix(resultSet.getFloat("prix"));
                        article.setQuantiter(resultSet.getInt("stock"));
                        article.setImage(resultSet.getString("image"));
                        articlesList.add(article);
                    }

                    // Convertir la liste d'articles en JSON
                    Gson gson = new Gson();
                    JsonArray jsonArray = gson.toJsonTree(articlesList).getAsJsonArray();

                    // Envoi de la réponse JSON au client
                    exchange.sendResponseHeaders(200, jsonArray.toString().length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(jsonArray.toString().getBytes());
                    os.close();
                } catch (SQLException e) {
                    // Gérer l'exception SQL ici
                    System.err.println("Erreur lors de l'exécution de la requête SQL : " + e.getMessage());
                    exchange.sendResponseHeaders(500, 0);  // Code 500 pour une erreur interne du serveur
                    OutputStream os = exchange.getResponseBody();
                    os.close();
                }
            } else {
                // Autre traitement pour les requêtes de méthode différente de GET
            }

        }
    }

}

