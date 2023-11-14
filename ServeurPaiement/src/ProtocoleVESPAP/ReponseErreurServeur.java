package ProtocoleVESPAP;

import ServeurGenerique.Reponse;

public class ReponseErreurServeur implements Reponse {
    public static int DATABASE_ERROR = 1;

    private int code;
    private String message;

    public ReponseErreurServeur(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
