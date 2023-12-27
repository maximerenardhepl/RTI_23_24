package Modele;

public class UserInfo {

    private String password;
    private String username;

    private boolean isConnected;

    public UserInfo(String Password, String Username)
    {
        password = password;
        username = Username;
        isConnected = true;
    }

    public UserInfo() {
        isConnected = false;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isConnected() { return isConnected; }

    public void connect() {
        isConnected = true;
    }

    public void disconnect() {
        isConnected = false;
    }
}
