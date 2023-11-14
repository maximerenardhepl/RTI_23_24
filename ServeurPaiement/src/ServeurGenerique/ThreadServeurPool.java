package ServeurGenerique;

import Logging.Logger;
import ProtocoleVESPAP.VESPAP;

import java.io.IOException;
import java.net.Socket;

public class ThreadServeurPool extends ThreadServeur {
    private FileAttente connexionsEnAttente;
    private ThreadGroup pool;
    private int taillePool;

    public ThreadServeurPool(int port, Protocole protocole, int taillePool, Logger logger) throws IOException {
        super(port, protocole, logger);
        connexionsEnAttente = new FileAttente();
        pool = new ThreadGroup("POOL");
        this.taillePool = taillePool;
    }

    @Override
    public void run() {
        logger.Trace("Démarrage du TH Serveur (Pool)...");

        for(int i = 0; i < taillePool; i++) {
            new ThreadClientPool(protocole, connexionsEnAttente, pool, logger).start();
        }

        while(!this.isInterrupted()) {
            Socket socketClient;
            try {
                socketServeur.setSoTimeout(300000);
                socketClient = socketServeur.accept();
                logger.Trace("Connexion acceptée, mise en file d'attente.");
                connexionsEnAttente.addConnexion(socketClient);

            }
            catch (IOException e) {
                logger.Trace("Erreur I/O");
            }
        }
        logger.Trace("TH Serveur (Pool) interrompu.");
        pool.interrupt();
        protocole.close(); //Va permettre de fermer la connexion a la base de données
                            //Permettrait également de pouvoir par exemple fermer d'autres ressources ouvertes par le protocole.
    }
}
