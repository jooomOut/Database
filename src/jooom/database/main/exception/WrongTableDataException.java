package jooom.database.main.exception;

public class WrongTableDataException extends RuntimeException {
    private final static String MSG = "테이블 정보가 잘못되었습니다.";
    public WrongTableDataException() {
        super(MSG);
    }
}
