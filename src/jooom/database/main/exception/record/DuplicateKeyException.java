package jooom.database.main.exception.record;

public class DuplicateKeyException extends RuntimeException {
    private final static String MSG = "이미 존재하는 키 입니다.";

    public DuplicateKeyException(String key) {
        super(MSG + " - " + key);
    }
}
