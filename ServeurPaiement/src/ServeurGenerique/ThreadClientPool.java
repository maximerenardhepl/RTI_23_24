package ServeurGenerique;

import Logging.Logger;

public class ThreadClientPool extends ThreadClient {
    private FileAttente connexionsEnAttente;

    public ThreadClientPool(Protocole protocole, FileAttente file, ThreadGroup groupe, Logger logger) {
        super(protocole, groupe, logger);
        connexionsEnAttente = file;
    }

    @Override
    public void run() {
        logger.Trace("TH Client (Pool) démarre...");
        boolean interrompu = false;

        while(!interrompu) {
            try {
                logger.Trace("Attente d'une connexion...");
                socketClient = connexionsEnAttente.getConnexion();
                logger.Trace("Connexion prise en charge");
                super.run();
            }
            catch (InterruptedException e) {
                System.out.println("Demande d'interruption...");
                interrompu = true;
            }
        }
        logger.Trace("TH Client (Pool) se termine.");
    }
}
