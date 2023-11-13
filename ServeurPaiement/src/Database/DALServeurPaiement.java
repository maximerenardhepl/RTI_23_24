package Database;

import ProtocoleVESPAP.RequeteGET_FACTURES;
import ProtocoleVESPAP.RequeteLOGIN;
import ProtocoleVESPAP.RequetePAY_FACTURE;
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
            String server = "192.168.129.21";
            connectionDB = new DatabaseConnection(DatabaseConnection.MYSQL, server, "PourStudent", "Student", "PassStudent1_");
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.logger = logger;
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
                int id = rs.getInt("id");
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

    public boolean payFacture(RequetePAY_FACTURE requete) throws DALException {
        int idFacture = requete.getIdFacture();
        try {
            String requeteSql = "UPDATE factures SET paye = 'true' WHERE id = " + idFacture + ";";
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
