package Classes.Requetes.Secure;

import Intefaces.Requete;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.*;

public class RequeteSecureGET_FACTURES implements Requete {
    private int idClient;
    private byte[] signature;

    public RequeteSecureGET_FACTURES(int idClient, PrivateKey clePriveeSignataire) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IOException, SignatureException {
        this.idClient = idClient;

        Signature s = Signature.getInstance("SHA1withRSA", "BC");
        s.initSign(clePriveeSignataire);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(idClient);

        s.update(baos.toByteArray());
        signature = s.sign();
    }

    public int getIdClient() { return idClient; }

    public boolean verifiySignature(PublicKey clePubliqueSignataire) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IOException, SignatureException {
        Signature s = Signature.getInstance("SHA1withRSA", "BC");
        s.initVerify(clePubliqueSignataire);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(idClient);
        s.update(baos.toByteArray());

        return s.verify(signature);
    }
}
