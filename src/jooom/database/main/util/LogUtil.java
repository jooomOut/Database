package jooom.database.main.util;

import java.util.logging.Logger;

public class LogUtil {
    private final static Logger log = Logger.getGlobal();

    public static void printTestTitle(String testName, String msg){
        System.out.printf("%-30s - %s\n", testName, msg);
    }
}
