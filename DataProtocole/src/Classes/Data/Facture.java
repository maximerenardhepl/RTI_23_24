package Classes.Data;

import java.io.Serializable;
import java.util.Date;

public class Facture implements Serializable {
    private String id;
    private Date date;
    private float montant;
    private boolean paye;

    public Facture(String id, Date date, float montant, boolean paye) {
        this.id = id;
        this.date = date;
        this.montant = montant;
        this.paye = paye;
    }

    public String getId() {
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

    public void setStatePaye(boolean paye) {
        this.paye = paye;
    }
}
