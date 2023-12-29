package Classes.Requetes.Secure;

import Intefaces.Requete;

public class RequeteSecureINIT implements Requete {
    private byte[] cleSessionCrypt; //clé de session cryptée asymétriquement

    public RequeteSecureINIT(byte[] cle) {
        this.cleSessionCrypt = cle;
    }
    public void setCleSessCrypt(byte[] cleSess) { cleSessionCrypt = cleSess; }
    public byte[] getCleSessCrypt() { return cleSessionCrypt; }
}
