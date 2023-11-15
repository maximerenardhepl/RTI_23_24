package Classes;

import Intefaces.Reponse;

public class ReponseLOGIN implements Reponse {
    private boolean valide;

    public ReponseLOGIN(boolean valide) {
        this.valide = valide;
    }

    public boolean isValide() {
        return valide;
    }
}
