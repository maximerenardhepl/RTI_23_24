package ServeurGenerique;

import Intefaces.Reponse;
import Intefaces.Requete;
import Logging.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class ThreadClient extends Thread {

    protected Protocole protocole;
    protected Socket socketClient;
    protected Logger logger;
    private int numero;

    private static int numCourant = 1;

    public ThreadClient(Protocole protocole, ThreadGroup groupe, Logger logger) {
        super(groupe, "TH Client" + numCourant + " (protocole=" + protocole.getNom() + ")");
        this.protocole = protocole;
        this.socketClient = null;
        this.logger = logger;
        this.numero = numCourant++;
    }

    public ThreadClient(Protocole protocole, Socket socket, Logger logger) throws IOException {
        super("TH Client " + numCourant + " (protocole=" + protocole.getNom() + ")");
        this.protocole = protocole;
        this.socketClient = socket;
        this.logger = logger;
        this.numero = numCourant++;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream oos = null;
            ObjectInputStream ois = null;

            try {
               oos = new ObjectOutputStream(socketClient.getOutputStream());
               ois = new ObjectInputStream(socketClient.getInputStream());

               while(true) {
                   Requete requete = (Requete) ois.readObject();
                   Reponse reponse = protocole.TraiteRequete(requete, socketClient);
                   oos.writeObject(reponse);
               }
            }
            catch (FinConnexionException e) {
                if(e.getReponse() != null) {
                    oos.writeObject(e.getReponse());
                }
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
            }
            catch (IOException e) {
                logger.Trace("Erreur fermeture socket");
            }
        }
    }


}
