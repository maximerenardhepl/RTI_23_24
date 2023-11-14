package Modele;

public class SocketInfos {
    private int port;
    private String ip;

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }

    public SocketInfos(int port, String ip) {
        this.port = port;
        this.ip = ip;
    }
}
