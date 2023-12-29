package ServeurGenerique;
import Intefaces.Reponse;
import Intefaces.Requete;

import javax.crypto.SecretKey;
import java.net.Socket;
import java.security.PrivateKey;

public interface ProtocoleSecurise {
    String getNom();
    Reponse TraiteRequete(Requete requete, Socket socket, PrivateKey clePriveeServeur, SecretKey cleSession)  throws FinConnexionException;

    void close();
}
