package ServeurGenerique;

import Classes.Reponses.Secure.ReponseSecureINIT;
import Intefaces.Reponse;
import Intefaces.Requete;
import Logging.Logger;
import jdk.jshell.spi.ExecutionControl;

import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;

public class ThreadClientSecure extends Thread {
    protected ProtocoleSecurise protocole;
    protected Socket socketClient;
    protected Logger logger;
    protected SecretKey cleSessionClient;
    private int numero;

    private static int numCourant = 1;

    //Modèle pool de threads
    public ThreadClientSecure(ProtocoleSecurise protocole, ThreadGroup groupe, Logger logger) {
        super(groupe, "TH Client" + numCourant + " (protocole=" + protocole.getNom() + ")");
        this.protocole = protocole;
        this.socketClient = null;
        this.logger = logger;
        this.numero = numCourant++;
    }

    //Modèle à la demande
    public ThreadClientSecure(ProtocoleSecurise protocole, Socket socket, Logger logger) throws IOException {
        super("TH Client " + numCourant + " (protocole=" + protocole.getNom() + ")");
        this.protocole = protocole;
        this.socketClient = socket;
        this.logger = logger;
        this.numero = numCourant++;
    }

    @Override
    public void run() {
        logger.Trace("Démarrage du ThreadClientSecure (Nom=" + this.getName() + ", Id=" + this.threadId() + ")");
        try {
            ObjectOutputStream oos = null;
            ObjectInputStream ois = null;

            try {
                oos = new ObjectOutputStream(socketClient.getOutputStream());
                ois = new ObjectInputStream(socketClient.getInputStream());

                PrivateKey clePriveeServeur = recupereClePriveeServeur();
                System.out.println("Clé privée Serveur: " + clePriveeServeur);

                Requete requete = (Requete) ois.readObject();
                Reponse reponse = protocole.TraiteRequete(requete, socketClient, clePriveeServeur, cleSessionClient);
                cleSessionClient = ((ReponseSecureINIT)reponse).getCleSession();
                System.out.println("Clé de session générée par le client: " + cleSessionClient);

                while(true) {
                    requete = (Requete) ois.readObject();
                    reponse = protocole.TraiteRequete(requete, socketClient, clePriveeServeur, cleSessionClient);
                    oos.writeObject(reponse);
                }
            }
            catch (FinConnexionException e) {
                if(e.getReponse() != null) {
                    oos.writeObject(e.getReponse());
                }
            } catch (UnrecoverableKeyException | CertificateException | KeyStoreException | NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
        catch (IOException e) {
            logger.Trace("Erreur I/O");
        }
        catch (ClassNotFoundException e) {
            logger.Trace("Erreur requete invalide");
        }
        finally {
            try {
                socketClient.close();
                logger.Trace("Fin du ThreadClientSecure (Nom=" + this.getName() + ", Id=" + this.threadId() + ")");
            }
            catch (IOException e) {
                logger.Trace("Erreur fermeture socket");
            }
        }
    }

    private PrivateKey recupereClePriveeServeur() throws IOException, ClassNotFoundException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {

        FileInputStream fis = new FileInputStream("../JavaKeyStores/KeyStore_ServeurPaiement");
        String keyStorePass = "serveurpaiement";
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(fis, keyStorePass.toCharArray());

        String keyPass = "cleserveur";
        PrivateKey cle = (PrivateKey) ks.getKey("cleserveurpaiement", keyPass.toCharArray());
        return cle;

        /*ObjectInputStream ois = new ObjectInputStream(new FileInputStream("../cles/clePriveeServeur.ser"));
        PrivateKey cle = (PrivateKey) ois.readObject();
        ois.close();
        return cle;*/
    }
}
