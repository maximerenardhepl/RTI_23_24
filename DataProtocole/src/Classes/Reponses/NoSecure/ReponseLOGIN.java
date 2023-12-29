package Classes.Reponses.NoSecure;

import Intefaces.Reponse;

public class ReponseLOGIN implements Reponse {
    private boolean valide;
    private String message;

    public ReponseLOGIN(boolean valide) {
        this.valide = valide;
    }
    public ReponseLOGIN(boolean valide, String message) {
        this.valide = valide;
        this.message = message;
    }

    public boolean isValide() {
        return valide;
    }
    public String getMessage() { return message; }
}
