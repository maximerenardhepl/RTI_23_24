package Classes.Requetes.NoSecure;

import Intefaces.Requete;

public class RequeteFACTURE_DETAILLEE implements Requete {
    private String idFacture;

    public RequeteFACTURE_DETAILLEE(String idFacture) {
        this.idFacture = idFacture;
    }

    public String getIdFacture() { return idFacture; }
}
