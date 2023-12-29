package Classes.Reponses.NoSecure;

import Intefaces.Reponse;

public class ReponsePAY_FACTURE implements Reponse {
    private boolean valide;
    private String message;

    public ReponsePAY_FACTURE(boolean valide) {
        this.valide = valide;
    }

    public ReponsePAY_FACTURE(boolean valide, String msg) {
        this.valide = valide;
        this.message = msg;
    }

    public boolean isValide() {
        return valide;
    }

    public String getMessage() { return message; }
}
