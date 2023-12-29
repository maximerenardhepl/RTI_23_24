import Controleur.Controleur;
import Vue.Principale;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Provider;
import java.security.Security;

//import com.formdev.flatlaf.FlatLightLaf;
    public class Main {
    public static void main(String[] args) {

        Security.addProvider(new BouncyCastleProvider());

        //Provider prov[] = Security.getProviders();
        //for (int i=0; i<prov.length; i++)
        //System.out.println(prov[i].getName() + "/" + prov[i].getVersion());

        //FlatLightLaf.setup();
        Principale vuePrincipale = new Principale();
        Controleur c = new Controleur(vuePrincipale);

        vuePrincipale.setControler(c);
        vuePrincipale.setVisible(true);
    }
}