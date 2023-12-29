package Classes.Requetes.Secure;

import Classes.Security.Crypto;
import Intefaces.Requete;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class RequeteSecurePAY_FACTURE implements Requete {
    private byte[] paimentData;

    public RequeteSecurePAY_FACTURE(String idFacture, String nom, String visa, SecretKey cleSession) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, NoSuchProviderException, InvalidKeyException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeUTF(idFacture);
        dos.writeUTF(nom);
        dos.writeUTF(visa);
        paimentData = Crypto.CryptSymDES(cleSession, baos.toByteArray());
    }

    public byte[] getPaimentData() { return paimentData; }
}
