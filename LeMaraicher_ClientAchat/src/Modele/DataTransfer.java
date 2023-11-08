package Modele;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Properties;

public class DataTransfer {
    //private String configPath = "\\config\\config.txt";
    private String configPath = "\\RTI_23_24\\LeMaraicher_ClientAchat\\config\\config.txt";
    private Socket s;

    public Socket getSocket() { return s; }

    public DataTransfer() {
        try {
            int port;
            if((port = getDefaultPort()) != -1) {
                System.out.println("port: " + port);
                s = new Socket("192.168.30.130", port);
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

        System.out.println("chemin complet: " + completePath);

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

    public int send(String data) {
        if(data.isEmpty()) {
            return -1;
        }
        else {
            int taille = data.length();
            String tailleStr = String.valueOf(taille);
            int nbZero = 4 - tailleStr.length();
            StringBuilder enTete = new StringBuilder();

            for(int i=0; i < nbZero; i++) {
                enTete.append('0');
            }
            enTete.append(tailleStr);

            StringBuilder request = new StringBuilder(enTete);
            request.append(data);
            int tailleReelle = request.length();

            try {
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                dos.writeBytes(request.toString());
            }
            catch(IOException e) {
                e.printStackTrace();
                return -1;
            }
            return tailleReelle;
        }
    }

    public String receive() throws IOException {
        StringBuilder reponse = new StringBuilder();
        String charsetName = "UTF-8";
        try {
            DataInputStream dis = new DataInputStream(s.getInputStream());

            int dataLength = 0;
            for(int i=0, j=3; i < 4; i++, j--) {
                byte b = dis.readByte();
                char c = (char)b;
                dataLength += Integer.parseInt(String.valueOf(c)) * Math.pow(10, j);
            }
            //System.out.println("taille de la charge utile: " + dataLength);

            byte[] bytes = dis.readNBytes(dataLength);
            reponse.append(new String(bytes, charsetName));
            //dis.close();
        }
        catch(IOException | NumberFormatException e) {
            e.printStackTrace();
            throw e;
        }
        return reponse.toString();
    }
}
