import Logging.ConsoleLogger;
import ProtocoleVESPAP.RequeteGET_FACTURES;
import ProtocoleVESPAP.RequeteLOGIN;
import ProtocoleVESPAP.VESPAP;
import ServeurGenerique.ThreadServeurPool;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        ConsoleLogger logger = new ConsoleLogger();
        VESPAP protocole = new VESPAP(logger);
        try {
            ThreadServeurPool threadServeurPool = new ThreadServeurPool(55000, protocole, 5, logger);
            threadServeurPool.start();
        }
        catch (IOException e) {
            logger.Trace("Erreur I/O lors du lancement du serveur!");
        }
    }
}