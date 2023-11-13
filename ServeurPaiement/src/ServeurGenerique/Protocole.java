package ServeurGenerique;

import java.net.Socket;

public interface Protocole {
    String getNom();
    Reponse TraiteRequete(Requete requete, Socket socket)  throws FinConnexionException;
}
