package ProtocoleVESPAP;

import ServeurGenerique.Requete;

public class RequeteLOGOUT implements Requete {
    private String login;

    public RequeteLOGOUT(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }
}
