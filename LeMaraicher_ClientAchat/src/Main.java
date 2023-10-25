import Controleur.Controler;
import Vues.MainView;

public class Main {
    public static void main(String[] args) {
        Controler c = new Controler();
        MainView vuePrincipale = new MainView();

        c.setRefView(vuePrincipale);
        vuePrincipale.setControler(c);

        vuePrincipale.setVisible(true);
    }
}