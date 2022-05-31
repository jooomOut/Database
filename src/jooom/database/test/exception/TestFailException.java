package jooom.database.test.exception;

public class TestFailException extends RuntimeException {
    private static final String MSG = "테스트에 실패하였습니다.";

    public TestFailException(String testName){
        super(MSG + " - " + testName);
    }
}
