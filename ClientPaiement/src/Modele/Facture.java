package Modele;

import java.util.Date;

public class Facture {
    private int id;
    private Date date;
    private float montant;
    private boolean paye;

    public Facture(int id, Date date, float montant, boolean paye) {
        this.id = id;
        this.date = date;
        this.montant = montant;
        this.paye = paye;
    }
}
