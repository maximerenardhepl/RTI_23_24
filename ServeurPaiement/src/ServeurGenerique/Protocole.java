package ServeurGenerique;

import Intefaces.Reponse;
import Intefaces.Requete;

import java.net.Socket;

public interface Protocole {
    String getNom();
    Reponse TraiteRequete(Requete requete, Socket socket)  throws FinConnexionException;

    void close();
}
