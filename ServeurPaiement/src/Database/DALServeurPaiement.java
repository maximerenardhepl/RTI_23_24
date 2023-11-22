package Database;

import Classes.*;
import Logging.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class DALServeurPaiement {
    private DatabaseConnection connectionDB;
    private Logger logger;

    public DALServeurPaiement(Logger logger) {
        try {
            //Il faut récupérer l'IP sur laquelle se trouve la BD pour l'affecter au parametre "server" de DatabaseConnection.
            String server = "10.222.20.117";
            connectionDB = new DatabaseConnection(DatabaseConnection.MYSQL, server, "PourStudent", "Student", "PassStudent1_");
        }
        catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        this.logger = logger;
    }

    public void close() {
        try {
            connectionDB.close();
        }
        catch(SQLException e) {
            logger.Trace("Erreur DatabaseConnection: " + e.getMessage());
        }
    }

    public boolean loginEmploye(RequeteLOGIN requete) throws DALException {
        String login = requete.getLogin();
        String password = requete.getPassword();
        try {
            String requeteSql = "SELECT password FROM employes WHERE login LIKE '" + login + "';";
            ResultSet rs = connectionDB.executeQuery(requeteSql);
            while(rs.next()) {
                String realPassword = rs.getString("password");
                if(password.equals(realPassword)) {
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        catch (SQLException e) {
            logger.Trace("Erreur DatabaseConnection: " + e.getMessage());
            throw new DALException("Une erreur est survenue lors de l'envoi de la requete!");
        }
        return false;
    }

    public ArrayList<Facture> getFactures(RequeteGET_FACTURES requete) throws DALException {
        int idClient = requete.getIdClient();
        try {
            String requeteSql = "SELECT id, date, montant, paye FROM factures WHERE idClient = " + idClient + ";";
            ResultSet rs = connectionDB.executeQuery(requeteSql);

            ArrayList<Facture> listeFactures = new ArrayList<>();
            while(rs.next()) {
                String id = rs.getString("id");
                Date date = rs.getDate("date");
                float montant = rs.getFloat("montant");
                boolean paye = rs.getBoolean("paye");

                Facture facture = new Facture(id, date, montant, paye);
                listeFactures.add(facture);
            }
            return listeFactures;
        }
        catch (SQLException e) {
            logger.Trace("Erreur DatabaseConnection: " + e.getMessage());
            throw new DALException("Une erreur est survenue lors de l'envoi de la requete!");
        }
    }

    public ArrayList<Article> getFactureDetaillee(RequeteFACTURE_DETAILLEE requete) throws DALException {
        String idFacture = requete.getIdFacture();
        try {
            String requeteSql = "SELECT idArticle, quantite FROM ventes WHERE idFacture LIKE '" + idFacture + "';";
            ResultSet rs = connectionDB.executeQuery(requeteSql);

            ArrayList<Article> listeArticles = new ArrayList<>();
            while(rs.next()) {
                int idArticle = rs.getInt("idArticle");
                int quantite = rs.getInt("quantite");

                String sousRequeteSql = "SELECT intitule, prix FROM articles WHERE id = " + idArticle + ";";
                ResultSet rsSousRequete = connectionDB.executeQuery(sousRequeteSql);

                String intituleArticle = null;
                float prixArticle = 0;
                while(rsSousRequete.next()) { //Normalement il ne doit y aovir qu'un seul tour de boucle en toute logique (requete de selection sur la clé primaire)...
                    intituleArticle = rsSousRequete.getString("intitule");
                    prixArticle = rsSousRequete.getFloat("prix");
                }
                Article nouvelArticle = new Article(idArticle, intituleArticle, quantite, prixArticle);
                listeArticles.add(nouvelArticle);
            }
            return listeArticles;
        }
        catch(SQLException e) {
            logger.Trace("Erreur DatabaseConnection: " + e.getMessage());
            throw new DALException("Une erreur est survenue lors de l'envoi de la requete!");
        }
    }

    public boolean payFacture(RequetePAY_FACTURE requete) throws DALException {
        String idFacture = requete.getIdFacture();
        try {
            String requeteSql = "UPDATE factures SET paye = true WHERE id = '" + idFacture + "';";
            int nbLignesAffectees = connectionDB.executeUpdate(requeteSql);
            if(nbLignesAffectees == 1) {
                return true;
            }
            else {
                return false;
            }
        }
        catch(SQLException e) {
            logger.Trace("Erreur DatabaseConnection: " + e.getMessage());
            throw new DALException("Une erreur est survenue lors de l'envoi de la requete!");
        }
    }
}
