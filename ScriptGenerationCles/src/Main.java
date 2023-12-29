import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.*;

public class Main {
    public static void main(String args[]) throws NoSuchAlgorithmException, NoSuchProviderException, IOException {

        Security.addProvider(new BouncyCastleProvider());
        KeyPairGenerator genCles = KeyPairGenerator.getInstance("RSA","BC");

        //Génération paire de clés pour Client
        genCles.initialize(512, new SecureRandom());
        KeyPair deuxCles = genCles.generateKeyPair();
        PublicKey clePublique = deuxCles.getPublic();
        PrivateKey clePrivee = deuxCles.getPrivate();
        System.out.println(" *** Cle publique generee = " + clePublique);
        System.out.println(" *** Cle privee generee = " + clePrivee);

        // Sérialisation des clés dans des fichiers différents
        System.out.println("Sérialisation de la clé publique dans le fichier clePubliqueClients.ser");
        ObjectOutputStream oos1 = new ObjectOutputStream(new FileOutputStream("../cles/clePubliqueClients.ser"));
        oos1.writeObject(clePublique);
        oos1.close();

        System.out.println("Sérialisation de la clé privée dans le fichier clePriveeClients.ser");
        ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream("../cles/clePriveeClients.ser"));
        oos2.writeObject(clePrivee);
        oos2.close();


        //Génération paire de clés pour Serveur
        genCles.initialize(512, new SecureRandom());
        deuxCles = genCles.generateKeyPair();
        clePublique = deuxCles.getPublic();
        clePrivee = deuxCles.getPrivate();
        System.out.println(" *** Cle publique generee = " + clePublique);
        System.out.println(" *** Cle privee generee = " + clePrivee);

        // Sérialisation des clés dans des fichiers différents
        System.out.println("Sérialisation de la clé publique dans le fichier clePubliqueServeur.ser");
        oos1 = new ObjectOutputStream(new FileOutputStream("../cles/clePubliqueServeur.ser"));
        oos1.writeObject(clePublique);
        oos1.close();

        System.out.println("Sérialisation de la clé privée dans le fichier clePriveeServeur.ser");
        oos2 = new ObjectOutputStream(new FileOutputStream("../cles/clePriveeServeur.ser"));
        oos2.writeObject(clePrivee);
        oos2.close();
    }
}