package ServeurGenerique;

import Logging.Logger;

import java.io.IOException;
import java.net.Socket;

public class ThreadClientDemande extends ThreadClient {

    public ThreadClientDemande(Protocole protocole, Socket socket, Logger logger) throws IOException {
        super(protocole, socket, logger);
    }

    @Override
    public void run() {
        logger.Trace("TH Client (Demande) démarre...");
        super.run();
        logger.Trace("TH Client (Demande) se termine");
    }
}
