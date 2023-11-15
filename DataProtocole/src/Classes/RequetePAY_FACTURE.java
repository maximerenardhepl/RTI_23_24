package Classes;

import Intefaces.Requete;

public class RequetePAY_FACTURE implements Requete {
    private int idFacture;
    private String nom;
    private String visa;

    public RequetePAY_FACTURE(int idFacture, String nom, String visa) {
        this.idFacture = idFacture;
        this.nom = nom;
        this.visa = visa;
    }

    public int getIdFacture() {
        return idFacture;
    }

    public String getNom() {
        return nom;
    }

    public String getVisa() {
        return visa;
    }
}
