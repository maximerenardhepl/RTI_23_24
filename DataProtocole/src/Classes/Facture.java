package Classes;

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

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public float getMontant() {
        return montant;
    }

    public boolean isPaye() {
        return paye;
    }
}
