import Controleur.Controler;
import Modele.Ovesp;
import Vues.ConnectionView;
import Vues.MainView;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLaf;

public class Main {
    public static void main(String[] args) {

        FlatDarculaLaf.setup();

        Ovesp.getInstance().init(); //Permet d'initialiser la Socket...
        Controler c = new Controler();
        ConnectionView connectionView = new ConnectionView();
        connectionView.setControler(c);
        c.setRefConnectionView(connectionView);
        connectionView.setVisible(true);
    }
}