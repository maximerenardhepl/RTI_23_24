package Modele.Exceptions;

public class AchatArticleException extends Exception {

    public static final int INSUFFICIENT_STOCK = 3;
    private int code;

    public AchatArticleException() {
        super();
    }

    public AchatArticleException(String msg, int code) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
