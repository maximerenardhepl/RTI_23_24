package Classes.Reponses.Secure;

import Intefaces.Reponse;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class ReponseSecurePAY_FACTURE implements Reponse {
    private boolean valide;
    private String message;
    private byte[] hmac;

    public ReponseSecurePAY_FACTURE(boolean valide, SecretKey cleSession) throws NoSuchAlgorithmException, IOException, NoSuchProviderException, InvalidKeyException {
        this(valide, cleSession, "");
    }

    public ReponseSecurePAY_FACTURE(boolean valide, SecretKey cleSession, String message) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IOException {
        this.valide = valide;
        this.message = message;

        Mac hm = Mac.getInstance("HMAC-MD5", "BC");
        hm.init(cleSession);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeBoolean(valide);
        dos.writeUTF(message);

        hm.update(baos.toByteArray());
        hmac = hm.doFinal();
    }

    public boolean isValide() { return valide; }

    public String getMessage() { return message; }

    public boolean verifyAuthenticity(SecretKey cleSession) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IOException {
        Mac hm = Mac.getInstance("HMAC-MD5", "BC");
        hm.init(cleSession);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeBoolean(valide);
        dos.writeUTF(message);

        hm.update(baos.toByteArray());
        byte[] hmacLocal = hm.doFinal();

        return MessageDigest.isEqual(hmac, hmacLocal);
    }
}
