package Modele.Exceptions;

public class DataBaseException extends Exception{

    public static final int QUERY_ERROR = 1;
    public static final int EMPTY_RESULT_SET = 2;
    private int code;

    public DataBaseException() {
        super();
    }
    public DataBaseException(String msg, int code) {
        super(msg);
        this.code = code;
    }

    int getCode() { return code; }
}
