import Logging.ConsoleLogger;
import Protocoles.VESPAP;
import Protocoles.VESPAPS;
import ServeurGenerique.ThreadServeurDemande;

import java.io.IOException;

public class MainServeurDemande {

    public static void main(String[] args) {

        ConsoleLogger logger = new ConsoleLogger();
        VESPAP protocole = new VESPAP(logger);
        try {
            ThreadServeurDemande threadServeur = new ThreadServeurDemande(60000, protocole, logger);
            threadServeur.start();
        }
        catch (IOException e) {
            logger.Trace("Erreur I/O lors du lancement du serveur!");
        }
    }
}
