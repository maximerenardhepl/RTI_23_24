package Database;

import ServeurGenerique.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class DALServeurPaiement {
    private DatabaseConnection connectionDB;
    private Logger logger;

    public DALServeurPaiement() {
        try {
            connectionDB = new DatabaseConnection(DatabaseConnection.MYSQL, "", "PourStudent", "Student", "PassStudent1_");
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean loginEmploye(String login, String password) {
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
        }
        return false;
    }

    public ArrayList<Facture> getFactures(int idClient) {
        ArrayList<Facture> listeFactures = null;
        try {
            String requeteSql = "SELECT id, date, montant, paye FROM factures WHERE idClient = " + idClient + ";";
            ResultSet rs = connectionDB.executeQuery(requeteSql);

            listeFactures = new ArrayList<>();
            while(rs.next()) {
                int id = rs.getInt("id");
                Date date = rs.getDate("date");
                float montant = rs.getFloat("montant");
                boolean paye = rs.getBoolean("paye");

                Facture facture = new Facture(id, date, montant, paye);
                listeFactures.add(facture);
            }
        }
        catch (SQLException e) {
            logger.Trace("Erreur DatabaseConnection: " + e.getMessage());
        }
        return listeFactures;
    }

    public boolean payFacture(int idFacture) {
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
        }
        return false;
    }
}
