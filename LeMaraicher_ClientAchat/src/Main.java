import Controleur.Controler;
import Modele.Ovesp;
import Vues.ConnectionView;
import Vues.MainView;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

public class Main {
    public static void main(String[] args) {

        FlatLightLaf.setup();

        Controler c = new Controler();
        ConnectionView connectionView = new ConnectionView();
        connectionView.setControler(c);
        c.setRefConnectionView(connectionView);
        connectionView.setVisible(true);
    }
}