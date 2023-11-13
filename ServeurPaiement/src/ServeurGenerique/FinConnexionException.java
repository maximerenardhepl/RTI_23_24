package ServeurGenerique;

public class FinConnexionException extends Exception {
    private Reponse reponse;

    public FinConnexionException(Reponse reponse) {
        super("Fin de la connexion...");
        this.reponse = reponse;
    }

    public Reponse getReponse() { return reponse; }
}
