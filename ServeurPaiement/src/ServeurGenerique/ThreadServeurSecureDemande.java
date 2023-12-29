package ServeurGenerique;

import Logging.Logger;

import java.io.IOException;
import java.net.Socket;

public class ThreadServeurSecureDemande extends ThreadServeurSecure {

    public ThreadServeurSecureDemande(int port, ProtocoleSecurise protocole, Logger logger) throws IOException {
        super(port, protocole, logger);
    }

    @Override
    public void run() {
        logger.Trace("Démarrage du TH Serveur Secure (Demande)...");
        while(!this.isInterrupted()) {
            Socket socketClient;
            try {
                //socketServeur.setSoTimeout(3000);
                socketClient = socketServeur.accept();
                logger.Trace("Connexion acceptée, création TH Client");
                Thread th = new ThreadClientSecure(protocole, socketClient, logger);
                th.start();
            }
            catch(IOException e) {
                logger.Trace("Erreur I/O");
            }
        }
        logger.Trace("TH Serveur Secure (Demande) interrompu");

        try { socketServeur.close(); }
        catch (IOException e) { logger.Trace("Erreur I/O"); }
        protocole.close();
    }
}
