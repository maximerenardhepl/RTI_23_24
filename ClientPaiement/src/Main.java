import Controleur.Controleur;
import Vue.Principale;

public class Main {
    public static void main(String[] args) {
        Principale vuePrincipale = new Principale();
        Controleur c = new Controleur(vuePrincipale);

        vuePrincipale.setControler(c);
        vuePrincipale.setVisible(true);
    }
}