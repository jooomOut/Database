package jooom.database.test;

import jooom.database.main.DatabaseInterface;
import jooom.database.test.exception.TestFailException;

public class TestManager {
    private TableTest tableTest;
    private RecordTest recordTest;

    public TestManager(DatabaseInterface databaseInterface) {
        this.tableTest = new TableTest(databaseInterface);
        this.recordTest = new RecordTest(databaseInterface);
    }

    public void run(){
        try {
            tableTest.testAllMethods();
            recordTest.testAllMethods();
            tableTest.clearTestTable();
        } catch (TestFailException e){
            tableTest.clearTestTable();
            throw e;
        }

    }
}
