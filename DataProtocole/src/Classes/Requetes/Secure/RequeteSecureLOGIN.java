package Classes.Requetes.Secure;

import Intefaces.Requete;

import javax.xml.crypto.Data;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;

public class RequeteSecureLOGIN implements Requete {
    private String login;
    private long temps;
    private double aleatoire;
    private byte[] digest;

    public RequeteSecureLOGIN(String login, String password) throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
        this.login = login;
        this.temps = new Date().getTime();
        this.aleatoire = Math.random();

        MessageDigest md = MessageDigest.getInstance("SHA-1", "BC");
        md.update(login.getBytes());
        md.update(password.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeLong(temps);
        dos.writeDouble(aleatoire);
        md.update(baos.toByteArray());
        digest = md.digest();
    }

    public String getLogin() {
        return login;
    }

    public boolean verifiyPassword(String password) throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
        MessageDigest md = MessageDigest.getInstance("SHA-1", "BC");
        md.update(login.getBytes());
        md.update(password.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeLong(temps);
        dos.writeDouble(aleatoire);
        md.update(baos.toByteArray());
        byte[] digestLocal = md.digest();

        return MessageDigest.isEqual(digest, digestLocal);
    }
}
