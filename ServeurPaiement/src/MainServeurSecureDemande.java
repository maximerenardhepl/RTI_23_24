import Logging.ConsoleLogger;
import Protocoles.VESPAP;
import Protocoles.VESPAPS;
import ServeurGenerique.InfoServer;
import ServeurGenerique.ThreadServeurDemande;
import ServeurGenerique.ThreadServeurSecure;
import ServeurGenerique.ThreadServeurSecureDemande;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.security.Security;

public class MainServeurSecureDemande {

    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());

        ConsoleLogger logger = new ConsoleLogger();
        VESPAPS protocole = new VESPAPS(logger);
        try {
            int port = InfoServer.getDefaultPort();
            ThreadServeurSecureDemande threadServeur = new ThreadServeurSecureDemande(port, protocole, logger);
            threadServeur.start();
        }
        catch (IOException e) {
            logger.Trace("Erreur I/O lors du lancement du serveur!");
        }
    }
}
