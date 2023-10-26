import Controleur.Controler;
import Modele.Ovesp;
import Vues.MainView;

public class Main {
    public static void main(String[] args) {
        if(Ovesp.getInstance() == null) {
            System.out.println("bonjour!");
        }

        Controler c = new Controler();
        MainView vuePrincipale = new MainView();

        c.setRefView(vuePrincipale);
        vuePrincipale.setControler(c);

        vuePrincipale.setVisible(true);
    }
}