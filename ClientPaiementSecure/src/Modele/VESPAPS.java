package Modele;

import Classes.Data.Article;
import Classes.Data.Facture;
import Classes.Reponses.NoSecure.ReponseErreurServeur;
import Classes.Reponses.NoSecure.ReponseLOGIN;
import Classes.Reponses.Secure.ReponseSecureGET_FACTURES;
import Classes.Requetes.NoSecure.RequeteLOGOUT;
import Classes.Requetes.Secure.RequeteSecurePAY_FACTURE;
import Classes.Requetes.Secure.RequeteSecureFACTURE_DETAILLEE;
import Classes.Reponses.Secure.ReponseSecurePAY_FACTURE;
import Classes.Reponses.Secure.ReponseSecureFACTURE_DETAILLEE;
import Classes.Requetes.Secure.*;
import Classes.Security.Crypto;
import Intefaces.Reponse;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

public class VESPAPS {
    Communication communication;
    private static VESPAPS instance;
    private ArrayList<Facture> listeFacture;
    private UserInfo infoclient;

    private SecretKey cleSession;

    public ArrayList<Facture> getListeFacture() {
        return listeFacture;
    }
    public boolean isClientConnected() {
        return infoclient.isConnected();
    }

    private VESPAPS() {
        communication = new Communication();
        try {
            communication.init();
        }
        catch(IOException e) {
            System.out.println("Une erreur est survenue lors de la tentative de connexion au serveur...Veuillez réessayer!");
        }

        listeFacture = new ArrayList<>();
        infoclient = new UserInfo();
        try {
            generateAndShareSessionKey();
        } catch (NoSuchAlgorithmException | NoSuchProviderException | IOException | ClassNotFoundException |
                 NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException |
                 CertificateException | KeyStoreException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateAndShareSessionKey() throws NoSuchAlgorithmException, NoSuchProviderException, IOException, ClassNotFoundException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, CertificateException, KeyStoreException {
        Security.addProvider(new BouncyCastleProvider());
        KeyGenerator keyGen = KeyGenerator.getInstance("DES", "BC");
        keyGen.init(new SecureRandom());
        cleSession = keyGen.generateKey();
        System.out.println("Clé de session générée: " + cleSession);

        PublicKey publicKeyServer = RecupereClePubliqueServeur();
        System.out.println("Clé publique Serveur: " + publicKeyServer);
        byte[] cleSessCrypt = Crypto.CryptAsymRSA(publicKeyServer, cleSession.getEncoded());

        RequeteSecureINIT requeteInit = new RequeteSecureINIT(cleSessCrypt);
        communication.getWriter().writeObject(requeteInit);
    }

    public static VESPAPS getInstance() {
        if (instance == null) {
            instance = new VESPAPS();
        }
        return instance;
    }

    public boolean Login(String username, String password) throws Exception {
        RequeteSecureLOGIN requete = new RequeteSecureLOGIN(username, password);
        try {

            Reponse reponse = communication.traiteRequete(requete);
            if(reponse instanceof ReponseErreurServeur) {
                throw new Exception(((ReponseErreurServeur) reponse).getMessage());
            }
            else if(reponse instanceof ReponseLOGIN) {
                if(((ReponseLOGIN) reponse).isValide()) {
                    infoclient.setUsername(username);
                    infoclient.setPassword(password); //On stocke les informations du client connecté dans l'objet UserInfo de VESPAP.
                    infoclient.connect();
                    return true;
                }
                else { //La réponse login nous a retourné false
                    String msg = ((ReponseLOGIN) reponse).getMessage(); //On récupère donc le message d'erreur fourni.
                    throw new Exception(msg);
                }
            }
            else {
                throw new Exception("Une erreur inconnue est survenue...");
            }
        }
        catch(IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void Logout() {
        try
        {
            String username = infoclient.getUsername();
            RequeteLOGOUT requete = new RequeteLOGOUT(username);
            infoclient.disconnect(); //Faire passer le booleen "isConnected" à false dans l'objet UserInfo.
            communication.getWriter().writeObject(requete);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Facture> GetFactures(int numClient) throws Exception {
        PrivateKey clePriveeClient = RecupereClePriveeClient();
        RequeteSecureGET_FACTURES requete = new RequeteSecureGET_FACTURES(numClient, clePriveeClient);
        try {
            Reponse reponse = communication.traiteRequete(requete);
            if(reponse instanceof ReponseErreurServeur) {
                throw new Exception(((ReponseErreurServeur) reponse).getMessage());
            }
            else if(reponse instanceof ReponseSecureGET_FACTURES) {
                byte[] facturesCryptees = ((ReponseSecureGET_FACTURES) reponse).getListeFacturesCryptee();
                byte[] facturesDecrypt = Crypto.DecryptSymDES(cleSession, facturesCryptees);

                ByteArrayInputStream baos = new ByteArrayInputStream(facturesDecrypt);
                ObjectInputStream ois = new ObjectInputStream(baos);
                ArrayList<Facture> factures = (ArrayList<Facture>) ois.readObject();

                if(factures.size() > 0) {
                    return listeFacture = factures;
                }
                else {
                    throw new Exception("Aucune facture n'a été trouvé pour ce numéro de client!");
                }
            }
            else {
                throw new Exception("Une erreur inconnue est survenue...");
            }
        }
        catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean PayFacture(Facture facture, String nom, String visa) throws Exception {
        RequeteSecurePAY_FACTURE requete = new RequeteSecurePAY_FACTURE(facture.getId(), nom, visa, cleSession);
        try
        {
            Reponse reponse = communication.traiteRequete(requete);
            if(reponse instanceof ReponseErreurServeur) {
                throw new Exception(((ReponseErreurServeur) reponse).getMessage());
            }
            else if(reponse instanceof ReponseSecurePAY_FACTURE reponseSecure)
            {
                if(reponseSecure.verifyAuthenticity(cleSession)) {
                    if(reponseSecure.isValide()) {

                        return true;
                    }
                    else {
                        throw new Exception(((ReponseSecurePAY_FACTURE) reponse).getMessage());
                    }
                }
                else {
                    throw new Exception("Votre paiement n'a pas pu être effectué en raison de problèmes de sécurité");
                }

            }
            else {
                throw new Exception("Une erreur inconnue est survenue...");
            }
        }
        catch(IOException | ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Article> GetFactureDetaillee(Facture facture) throws Exception {
        PrivateKey clePriveeClient = RecupereClePriveeClient();
        RequeteSecureFACTURE_DETAILLEE requete = new RequeteSecureFACTURE_DETAILLEE(facture.getId(), clePriveeClient);
        try {
            Reponse reponse = communication.traiteRequete(requete);
            if(reponse instanceof ReponseErreurServeur) {
                throw new Exception(((ReponseErreurServeur) reponse).getMessage());
            }
            else if(reponse instanceof ReponseSecureFACTURE_DETAILLEE reponseSecure) {
                byte[] articlesCryptees = reponseSecure.getListeArticlesCryptee();
                byte[] articlesDecrypt = Crypto.DecryptSymDES(cleSession, articlesCryptees);

                ByteArrayInputStream baos = new ByteArrayInputStream(articlesDecrypt);
                ObjectInputStream ois = new ObjectInputStream(baos);
                ArrayList<Article> articles = (ArrayList<Article>) ois.readObject();

                if(articles.size() > 0) {
                    return articles;
                }
                else {
                    throw new Exception("Une erreur est survenue! La facture detaillee n'a pas pu etre affichee...");
                }
            }
            else {
                throw new Exception("Une erreur inconnue est survenue...");
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public PublicKey RecupereClePubliqueServeur() throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {

        FileInputStream fis = new FileInputStream("../JavaKeyStores/KeyStore_ClientPaiement");
        String keyStorePass = "clientpaiement";
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(fis, keyStorePass.toCharArray());

        X509Certificate certificat = (X509Certificate) ks.getCertificate("certificatserveur");
        PublicKey cle = certificat.getPublicKey();
        return cle;


        /*ObjectInputStream ois = new ObjectInputStream(new FileInputStream("../cles/clePubliqueServeur.ser"));
        PublicKey cle = (PublicKey) ois.readObject();
        ois.close();
        return cle;*/
    }

    public PrivateKey RecupereClePriveeClient() throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {

        FileInputStream fis = new FileInputStream("../JavaKeyStores/KeyStore_ClientPaiement");
        String keyStorePass = "clientpaiement";
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(fis, keyStorePass.toCharArray());

        String keyPass = "cleclient";
        PrivateKey cle = (PrivateKey) ks.getKey("cleclientpaiement", keyPass.toCharArray());
        return cle;

        /*ObjectInputStream ois = new ObjectInputStream(new FileInputStream("../cles/clePriveeClients.ser"));
        PrivateKey cle = (PrivateKey) ois.readObject();
        ois.close();
        return cle;*/
    }
}
