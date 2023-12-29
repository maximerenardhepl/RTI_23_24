package Classes.Reponses.Secure;

import Classes.Data.Article;
import Classes.Security.Crypto;
import Intefaces.Reponse;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;

public class ReponseSecureFACTURE_DETAILLEE implements Reponse {
    private byte[] listeArticles;

    public ReponseSecureFACTURE_DETAILLEE(ArrayList<Article> articles, SecretKey cleSession) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, NoSuchProviderException, InvalidKeyException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(articles);
        listeArticles = Crypto.CryptSymDES(cleSession, baos.toByteArray());
        oos.close();
    }

    public byte[] getListeArticlesCryptee() { return listeArticles; }
}
