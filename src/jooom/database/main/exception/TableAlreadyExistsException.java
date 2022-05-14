package jooom.database.main.exception;

import java.nio.file.FileAlreadyExistsException;

public class TableAlreadyExistsException extends FileAlreadyExistsException {
    private final static String MSG = "해당 테이블이 이미 존재합니다.";
    public TableAlreadyExistsException(String path) {
        super(path);
    }
}
