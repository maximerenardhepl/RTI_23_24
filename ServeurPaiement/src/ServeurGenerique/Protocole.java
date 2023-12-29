package ServeurGenerique;

import Intefaces.Reponse;
import Intefaces.Requete;

import javax.crypto.SecretKey;
import java.net.Socket;
import java.security.PrivateKey;

public interface Protocole {
    String getNom();
    Reponse TraiteRequete(Requete requete, Socket socket)  throws FinConnexionException;

    void close();
}
