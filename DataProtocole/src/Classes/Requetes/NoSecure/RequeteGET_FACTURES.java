package Classes.Requetes.NoSecure;

import Intefaces.Requete;

public class RequeteGET_FACTURES implements Requete {
    private int idClient;

    public RequeteGET_FACTURES(int idClient) {
        this.idClient = idClient;
    }

    public int getIdClient() {
        return idClient;
    }
}
