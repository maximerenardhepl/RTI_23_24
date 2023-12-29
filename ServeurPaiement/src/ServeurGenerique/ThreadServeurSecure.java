package ServeurGenerique;

import Logging.Logger;

import java.io.IOException;
import java.net.ServerSocket;

public class ThreadServeurSecure extends Thread {
    protected int port;
    protected ProtocoleSecurise protocole;
    protected Logger logger;
    protected ServerSocket socketServeur;


    public ThreadServeurSecure(int port, ProtocoleSecurise protocole, Logger logger) throws IOException {
        super("TH Serveur (port=" + port + ", protocole=" + protocole.getNom() + ")");
        this.port = port;
        this.protocole = protocole;
        this.logger = logger;
        socketServeur = new ServerSocket(port);
    }
}
