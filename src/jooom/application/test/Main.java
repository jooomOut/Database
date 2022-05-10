package jooom.application.test;

import jooom.database.DatabaseInterface;
import jooom.database.DatabaseInterfaceImpl;
import jooom.database.test.TestManager;

public class Main {
    private static DatabaseInterface databaseInterface = new DatabaseInterfaceImpl();
    private static TestManager testManager = new TestManager();
    public static void main(String[] args) {
        doTest();
        run();
    }

    private static void doTest() {
        testManager.run();
    }

    private static void run() {


    }

}
