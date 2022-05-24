package jooom.database.main.exception.record;

public class TooLargeColumnException extends RuntimeException {
    private static final String MSG = "데이터가 너무 큽니다.";
    public TooLargeColumnException(String data, int size) {
        super(MSG + " - " + data + " - size is " + size);
    }
}
