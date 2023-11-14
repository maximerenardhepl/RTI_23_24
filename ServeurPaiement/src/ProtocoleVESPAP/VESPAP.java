package ProtocoleVESPAP;

import Database.DALException;
import Database.DALServeurPaiement;
import Database.Facture;
import Logging.Logger;
import ServeurGenerique.*;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class VESPAP implements Protocole {
    private DALServeurPaiement dal;
    private Logger logger;
    private HashMap<String, Socket> clientsConnectes;

    public VESPAP(Logger logger) {
        this.logger = logger;
        dal = new DALServeurPaiement(logger);
        clientsConnectes = new HashMap<>();
    }
    @Override
    public String getNom() {
        return "VESPAP";
    }

    @Override
    public synchronized Reponse TraiteRequete(Requete requete, Socket socket) throws FinConnexionException {
        if(requete instanceof RequeteLOGIN) {
            return TraiteRequeteLOGIN((RequeteLOGIN) requete, socket);
        }
        if(requete instanceof RequeteGET_FACTURES) {
            return TraiteRequeteGET_FACTURES((RequeteGET_FACTURES) requete);
        }
        if(requete instanceof RequetePAY_FACTURE) {
            return TraiteRequetePAY_FACTURES((RequetePAY_FACTURE) requete);
        }
        if(requete instanceof RequeteLOGOUT) {
            TraiteRequeteLOGOUT((RequeteLOGOUT) requete);
        }
        return null;
    }

    @Override
    public void close() {
        dal.close();
    }

    private synchronized ReponseLOGIN TraiteRequeteLOGIN(RequeteLOGIN requete, Socket socket) throws FinConnexionException {
        logger.Trace("RequeteLOGIN reçue de " + requete.getLogin());
        try {
            boolean areIdentifiantsOk = dal.loginEmploye(requete);
            if(areIdentifiantsOk) {
                String ipPortClient = socket.getInetAddress().getHostAddress() + "/" + socket.getPort();
                logger.Trace(requete.getLogin() + " correctement loggé. En provenance de " + ipPortClient);

                //Ajout du client dans la HashMap des clients connectes + creation de la reponse.
                clientsConnectes.put(requete.getLogin(), socket);
                return new ReponseLOGIN(true);
            }
            return new ReponseLOGIN(false);
        }
        catch (DALException e) {
            ReponseErreurServeur reponseErr = new ReponseErreurServeur(ReponseErreurServeur.DATABASE_ERROR, e.getMessage());
            throw new FinConnexionException(reponseErr);
        }
    }

    private synchronized ReponseGET_FACTURES TraiteRequeteGET_FACTURES(RequeteGET_FACTURES requete) throws FinConnexionException {
        logger.Trace("RequeteGET_FACTURES reçue");
        try {
            ArrayList<Facture> listeFactures = dal.getFactures(requete);
            return new ReponseGET_FACTURES(listeFactures);
        }
        catch (DALException e) {
            ReponseErreurServeur reponseErr = new ReponseErreurServeur(ReponseErreurServeur.DATABASE_ERROR, e.getMessage());
            throw new FinConnexionException(reponseErr);
        }
    }

    private synchronized ReponsePAY_FACTURE TraiteRequetePAY_FACTURES(RequetePAY_FACTURE requete) throws FinConnexionException {
        logger.Trace("RequetePAY_FACTURE reçue");
        try {
            if(isVisaOk(requete.getVisa())) {
                boolean isPaiementEffectue = dal.payFacture(requete);
                if(isPaiementEffectue) {
                    return new ReponsePAY_FACTURE(true);
                }
                return new ReponsePAY_FACTURE(false, "Le paiement n'a pas pu être effectué...");
            }
            return new ReponsePAY_FACTURE(false, "Le numéro de carte VISA est invalide!");
        }
        catch (DALException e) {
            ReponseErreurServeur reponseErr = new ReponseErreurServeur(ReponseErreurServeur.DATABASE_ERROR, e.getMessage());
            throw new FinConnexionException(reponseErr);
        }
    }

    private boolean isVisaOk(String visa) {
        if(visa.length() == 16) {
            for(char c : visa.toCharArray()) {
                if(!Character.isDigit(c)) {
                    return false;
                }
            }
        }
        return false;
    }

    private synchronized void TraiteRequeteLOGOUT(RequeteLOGOUT requete) throws FinConnexionException {
        logger.Trace("RequeteLOGOUT reçue de " + requete.getLogin());
        clientsConnectes.remove(requete.getLogin());
        logger.Trace(requete.getLogin() + " correctement déloggé");
        throw new FinConnexionException(); //Permet de signaler au ThreadClient que l'échange entre le serveur et le client est terminé -> il y aura fermeture de la socket...
    }
}
