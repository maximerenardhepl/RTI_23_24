package Protocoles;

import Classes.Data.Article;
import Classes.Data.Facture;
import Classes.Reponses.NoSecure.ReponseErreurServeur;
import Classes.Reponses.NoSecure.ReponseLOGIN;
import Classes.Reponses.Secure.ReponseSecureFACTURE_DETAILLEE;
import Classes.Reponses.Secure.ReponseSecureGET_FACTURES;
import Classes.Reponses.Secure.ReponseSecureINIT;
import Classes.Reponses.Secure.ReponseSecurePAY_FACTURE;
import Classes.Requetes.NoSecure.RequeteLOGOUT;
import Classes.Requetes.Secure.*;
import Classes.Security.Crypto;
import Database.DALException;
import Database.DALServeurPaiement;
import Intefaces.Reponse;
import Intefaces.Requete;
import Logging.Logger;
import ServeurGenerique.FinConnexionException;
import ServeurGenerique.ProtocoleSecurise;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class VESPAPS implements ProtocoleSecurise {

    private DALServeurPaiement dal;
    private Logger logger;
    private HashMap<String, Socket> clientsConnectes;

    public VESPAPS(Logger logger) {
        this.logger = logger;
        dal = new DALServeurPaiement(logger);
        clientsConnectes = new HashMap<>();
    }

    @Override
    public String getNom() {
        return "VESPAPS";
    }

    @Override
    public Reponse TraiteRequete(Requete requete, Socket socket, PrivateKey clePriveeServeur, SecretKey cleSession) throws FinConnexionException {
        try {
            if(requete instanceof RequeteSecureINIT) {
                return TraiteRequeteSecureINIT((RequeteSecureINIT) requete, clePriveeServeur);
            }
            if(requete instanceof RequeteSecureLOGIN) {
                return TraiteRequeteSecureLOGIN((RequeteSecureLOGIN) requete, socket);
            }
            if(requete instanceof RequeteSecureGET_FACTURES) {
                return TraiteRequeteSecureGET_FACTURES((RequeteSecureGET_FACTURES) requete, cleSession, socket);
            }
            if(requete instanceof RequeteSecurePAY_FACTURE) {
                return TraiteRequeteSecurePAY_FACTURE((RequeteSecurePAY_FACTURE) requete, cleSession, socket);
            }
            if(requete instanceof RequeteSecureFACTURE_DETAILLEE) {
                return TraiteRequeteSecureFACTURE_DETAILLEE((RequeteSecureFACTURE_DETAILLEE) requete, cleSession, socket);
            }
            if(requete instanceof RequeteLOGOUT) {
                TraiteRequeteLOGOUT((RequeteLOGOUT) requete);
            }
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | NoSuchProviderException | InvalidKeyException | IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private synchronized ReponseLOGIN TraiteRequeteSecureLOGIN(RequeteSecureLOGIN requete, Socket socket) throws FinConnexionException {
        logger.Trace("RequeteSecureLOGIN reçue de " + requete.getLogin());
        try {
            if(!clientsConnectes.containsKey(requete.getLogin())) {
                boolean areIdentifiantsOk = dal.loginEmploye(requete);
                if(areIdentifiantsOk) {
                    String ipPortClient = socket.getInetAddress().getHostAddress() + "/" + socket.getPort();
                    logger.Trace(requete.getLogin() + " correctement loggé. En provenance de " + ipPortClient);

                    //Ajout du client dans la HashMap des clients connectes + creation de la reponse.
                    clientsConnectes.put(requete.getLogin(), socket);
                    return new ReponseLOGIN(true);
                }
                return new ReponseLOGIN(false, "Le nom d'utilisateur ou le mot passe entré est incorrect!");
            }
            else {
                return new ReponseLOGIN(false, "Connexion impossible! Ce client est déjà connecté...");
            }
        }
        catch (DALException e) {
            ReponseErreurServeur reponseErr = new ReponseErreurServeur(ReponseErreurServeur.DATABASE_ERROR, e.getMessage());
            throw new FinConnexionException(reponseErr);
        }
    }

    private synchronized ReponseSecureINIT TraiteRequeteSecureINIT(RequeteSecureINIT requete, PrivateKey clePriveeServeur) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, NoSuchProviderException, InvalidKeyException {
        byte[] cleSessionDecrypt;
        cleSessionDecrypt = Crypto.DecryptAsymRSA(clePriveeServeur, requete.getCleSessCrypt());
        SecretKey cleSession = new SecretKeySpec(cleSessionDecrypt, "DES");
        return new ReponseSecureINIT(cleSession);
    }

    private synchronized ReponseSecureGET_FACTURES TraiteRequeteSecureGET_FACTURES(RequeteSecureGET_FACTURES requete, SecretKey cleSession, Socket socketClient) throws FinConnexionException {
        String ipPortClient = socketClient.getInetAddress().getHostAddress() + "/" + socketClient.getPort();
        logger.Trace("RequeteSecureGET_FACTURES reçue de " + ipPortClient);
        try {
            PublicKey clePubliqueClient = recupereClePubliqueClient();
            if(requete.verifiySignature(clePubliqueClient)) {
                ArrayList<Facture> listeFactures = dal.getFactures(requete.getIdClient());
                return new ReponseSecureGET_FACTURES(listeFactures, cleSession);
            }
            else {
                String msgErr = "Signature en provenance du client invalide!";
                ReponseErreurServeur reponseErr = new ReponseErreurServeur(ReponseErreurServeur.SECURITY_ERROR_INVALID_SIGNATURE, msgErr);
                throw new FinConnexionException(reponseErr);
            }
        }
        catch(DALException e) {
            ReponseErreurServeur reponseErr = new ReponseErreurServeur(ReponseErreurServeur.DATABASE_ERROR, e.getMessage());
            throw new FinConnexionException(reponseErr);
        }
        catch (NoSuchPaddingException | IllegalBlockSizeException | IOException | NoSuchAlgorithmException |
               BadPaddingException | NoSuchProviderException | InvalidKeyException | SignatureException |
               ClassNotFoundException | CertificateException | KeyStoreException e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized ReponseSecurePAY_FACTURE TraiteRequeteSecurePAY_FACTURE(RequeteSecurePAY_FACTURE requete, SecretKey cleSession, Socket socketClient) throws FinConnexionException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, NoSuchProviderException, InvalidKeyException, IOException {
        String ipPortClient = socketClient.getInetAddress().getHostAddress() + "/" + socketClient.getPort();
        logger.Trace("RequetePAY_FACTURE reçue de " + ipPortClient);

        byte[] paimentData = Crypto.DecryptSymDES(cleSession, requete.getPaimentData());
        logger.Trace("Données du paiement reçues (cryptées) : " + Arrays.toString(paimentData));

        ByteArrayInputStream bais = new ByteArrayInputStream(paimentData);
        DataInputStream dis = new DataInputStream(bais);
        String idFacture = dis.readUTF();
        String nom = dis.readUTF();
        String visa = dis.readUTF();

        try {
            if(isVisaOk(visa)) {
                boolean isPaiementEffectue = dal.payFacture(idFacture);
                if(isPaiementEffectue) {
                    return new ReponseSecurePAY_FACTURE(true, cleSession);
                }
                return new ReponseSecurePAY_FACTURE(false, cleSession, "Le paiement n'a pas pu être effectué...");
            }
            return new ReponseSecurePAY_FACTURE(false, cleSession, "Le numéro de carte VISA est invalide!");
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
            return true;
        }
        else
        {
            return false;
        }
    }

    private synchronized ReponseSecureFACTURE_DETAILLEE TraiteRequeteSecureFACTURE_DETAILLEE(RequeteSecureFACTURE_DETAILLEE requete, SecretKey cleSession, Socket socketClient) throws FinConnexionException {
        String ipPortClient = socketClient.getInetAddress().getHostAddress() + "/" + socketClient.getPort();
        logger.Trace("RequeteFACTURE_DETAILLEE reçue de " + ipPortClient);
        try {
            PublicKey clePubliqueClient = recupereClePubliqueClient();
            if(requete.verifiySignature(clePubliqueClient)) {
                ArrayList<Article> listeArticles = dal.getFactureDetaillee(requete.getIdFacture());
                return new ReponseSecureFACTURE_DETAILLEE(listeArticles, cleSession);
            }
            else {
                String msgErr = "Signature en provenance du client invalide!";
                ReponseErreurServeur reponseErr = new ReponseErreurServeur(ReponseErreurServeur.SECURITY_ERROR_INVALID_SIGNATURE, msgErr);
                throw new FinConnexionException(reponseErr);
            }
        }
        catch(DALException e) {
            ReponseErreurServeur reponseErr = new ReponseErreurServeur(ReponseErreurServeur.DATABASE_ERROR, e.getMessage());
            throw new FinConnexionException(reponseErr);
        }
        catch (IOException | NoSuchAlgorithmException | SignatureException | NoSuchProviderException |
               InvalidKeyException | ClassNotFoundException | NoSuchPaddingException | IllegalBlockSizeException |
               BadPaddingException | CertificateException | KeyStoreException e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized void TraiteRequeteLOGOUT(RequeteLOGOUT requete) throws FinConnexionException {
        logger.Trace("RequeteLOGOUT reçue de " + requete.getLogin());
        clientsConnectes.remove(requete.getLogin());
        logger.Trace(requete.getLogin() + " correctement déloggé");
        throw new FinConnexionException(); //Permet de signaler au ThreadClient que l'échange entre le serveur et le client est terminé -> il y aura fermeture de la socket...
    }

    @Override
    public void close() { dal.close(); }

    private PublicKey recupereClePubliqueClient() throws IOException, ClassNotFoundException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
        FileInputStream fis = new FileInputStream("../JavaKeyStores/KeyStore_ServeurPaiement");
        String keyStorePass = "serveurpaiement";
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(fis, keyStorePass.toCharArray());

        X509Certificate certificat = (X509Certificate) ks.getCertificate("certificatclient");
        PublicKey cle = certificat.getPublicKey();
        return cle;

        /*ObjectInputStream ois = new ObjectInputStream(new FileInputStream("../cles/clePubliqueClients.ser"));
        PublicKey cle = (PublicKey) ois.readObject();
        ois.close();
        return cle;*/
    }
}
