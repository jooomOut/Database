package jooom.database.main.exception.record;

public class NoPrimaryKeyException extends RuntimeException {
    private final static String MSG = "기본 키가 존재하지 않습니다..";

    public NoPrimaryKeyException(String tableName) {
        super(MSG + " - " + tableName);
    }
}
