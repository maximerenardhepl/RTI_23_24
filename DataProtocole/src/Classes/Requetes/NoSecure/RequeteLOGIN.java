package Classes.Requetes.NoSecure;

import Intefaces.Requete;

public class RequeteLOGIN implements Requete {
    private String login;
    private String password;

    public RequeteLOGIN(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}