package Classes.Reponses.Secure;

import Intefaces.Reponse;

import javax.crypto.SecretKey;

public class ReponseSecureINIT implements Reponse {
    private SecretKey cleSession;

    public ReponseSecureINIT(SecretKey cle) {
        this.cleSession = cle;
    }

    public SecretKey getCleSession() {
        return cleSession;
    }

}
