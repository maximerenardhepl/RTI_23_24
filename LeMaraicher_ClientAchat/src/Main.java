import Controleur.Controler;
import Modele.Ovesp;
import Vues.ConnectionView;
import Vues.MainView;

public class Main {
    public static void main(String[] args) {
        Controler c = new Controler();
        ConnectionView connectionView = new ConnectionView();
        connectionView.setControler(c);
        c.setRefConnectionView(connectionView);
        connectionView.setVisible(true);
    }
}