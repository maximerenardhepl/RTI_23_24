package Modele;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Properties;

public class DataTransfer {
    private String configPath = "\\config\\config.txt";
    private Socket s;

    public DataTransfer() {
        try {
            int port;
            if((port = getDefaultPort()) != -1) {
                System.out.println("port: " + port);
                s = new Socket("10.59.22.30", port);
            }
        }
        catch(IOException e) {
            //JOptionPane.showMessageDialog(null, "Erreur lors de la tentative de connexion au serveur...");
            e.printStackTrace();
        }
    }

    private int getDefaultPort() {
        String rootDirectory = System.getProperty("user.dir");
        String completePath = rootDirectory + configPath;

        int defaultPort = -1;
        try {
            Properties prop = new Properties();
            FileInputStream fis = new FileInputStream(completePath);

            prop.load(fis);
            defaultPort = Integer.parseInt(prop.getProperty("defaultPort"));
            fis.close();
        }
        catch(IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return defaultPort;
    }
}
