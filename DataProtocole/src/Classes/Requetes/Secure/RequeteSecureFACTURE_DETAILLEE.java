package Classes.Requetes.Secure;

import Intefaces.Requete;

import javax.xml.crypto.Data;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.*;

public class RequeteSecureFACTURE_DETAILLEE implements Requete {
    private String idFacture;
    private byte[] signature;

    public RequeteSecureFACTURE_DETAILLEE(String idFacture, PrivateKey clePriveeSignataire) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        this.idFacture = idFacture;

        Signature s = Signature.getInstance("SHA1withRSA", "BC");
        s.initSign(clePriveeSignataire);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeUTF(idFacture);

        s.update(baos.toByteArray());
        signature = s.sign();
    }

    public String getIdFacture() { return idFacture; }

    public boolean verifiySignature(PublicKey clePubliqueSignataire) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IOException, SignatureException {
        Signature s = Signature.getInstance("SHA1withRSA", "BC");
        s.initVerify(clePubliqueSignataire);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeUTF(idFacture);
        s.update(baos.toByteArray());

        return s.verify(signature);
    }
}
