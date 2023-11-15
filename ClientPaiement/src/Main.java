import Controleur.Controleur;
import Vue.Principale;
import com.formdev.flatlaf.FlatLightLaf;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        Principale vuePrincipale = new Principale();
        Controleur c = new Controleur(vuePrincipale);

        vuePrincipale.setControler(c);
        vuePrincipale.setVisible(true);
    }
}