package Modele;

import java.util.ArrayList;

public class ReponseGET_FACTURES implements Reponse {
    private ArrayList<Facture> listeFactures;

    public ReponseGET_FACTURES(ArrayList<Facture> listeFactures) {
        this.listeFactures = listeFactures;
    }

    public ArrayList<Facture> getListeFactures() {
        return listeFactures;
    }
}
