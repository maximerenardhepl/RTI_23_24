package ServeurGenerique;

import Intefaces.Reponse;

public class FinConnexionException extends Exception {
    private Reponse reponse;

    public FinConnexionException(Reponse reponse) {
        super("Fin de la connexion...");
        this.reponse = reponse;
    }

    public FinConnexionException() {
        super("Fin de la connexion...");
        reponse = null;
    }

    public Reponse getReponse() { return reponse; }
}
