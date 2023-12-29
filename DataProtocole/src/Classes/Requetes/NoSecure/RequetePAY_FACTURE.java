package Classes.Requetes.NoSecure;

import Intefaces.Requete;

public class RequetePAY_FACTURE implements Requete {
    private String idFacture;
    private String nom;
    private String visa;

    public RequetePAY_FACTURE(String idFacture, String nom, String visa) {
        this.idFacture = idFacture;
        this.nom = nom;
        this.visa = visa;
    }

    public String getIdFacture() {
        return idFacture;
    }

    public String getNom() {
        return nom;
    }

    public String getVisa() {
        return visa;
    }
}
