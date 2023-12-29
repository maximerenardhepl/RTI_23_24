package ServeurGenerique;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class InfoServer {

    private static final String configServerFile = "\\config\\config.txt";

    public static int getDefaultPort() throws IOException {
        String rootDirectory = System.getProperty("user.dir");
        String completePath = rootDirectory + configServerFile;

        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream(completePath);

        prop.load(fis);
        int defaultPort = Integer.parseInt(prop.getProperty("port"));
        fis.close();
        return defaultPort;
    }

    public static String getDbServerIp() throws IOException {
        String rootDirectory = System.getProperty("user.dir");
        String completePath = rootDirectory + configServerFile;

        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream(completePath);

        prop.load(fis);
        String dbServerIp = prop.getProperty("DbServerIp");
        fis.close();
        return dbServerIp;
    }
}
