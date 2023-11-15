package Modele;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;
import Classes.*;
import Intefaces.*;

public class Communication {
    private Socket socketClient;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private String configPath = "\\config\\config.txt";
    public Communication() { }

    public void init() throws IOException {
        try {
            SocketInfos infos = getDefaultSocketInfos();
            socketClient = new Socket(infos.getIp(), infos.getPort());

            writer = new ObjectOutputStream(socketClient.getOutputStream());
            reader = new ObjectInputStream(socketClient.getInputStream());
        }
        catch(IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private SocketInfos getDefaultSocketInfos() throws IOException {
        String rootDirectory = System.getProperty("user.dir");
        String completePath = rootDirectory + configPath;

        try {
            Properties prop = new Properties();
            FileInputStream fis = new FileInputStream(completePath);

            prop.load(fis);
            int defaultPort = Integer.parseInt(prop.getProperty("defaultPort"));
            String defaultIp = prop.getProperty("ipServeur");
            SocketInfos infos = new SocketInfos(defaultPort, defaultIp);
            fis.close();
            return infos;
        }
        catch(IOException | NumberFormatException e) {
            throw e;
        }
    }

    public Reponse traiteRequete(Requete requete) throws IOException, ClassNotFoundException {
        if(requete instanceof RequeteLOGIN) {
            System.out.println("C'est une RequeteLOGIN...");
        }
        writer.writeObject(requete);
        return (Reponse) reader.readObject();
    }
}
