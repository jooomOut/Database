package jooom.database.test;

import jooom.database.main.DatabaseInterface;

public class TestManager {
    private TableTest tableTest;
    private RecordTest recordTest;

    public TestManager(DatabaseInterface databaseInterface) {
        this.tableTest = new TableTest(databaseInterface);
        this.recordTest = new RecordTest(databaseInterface);
    }

    public void run(){
        tableTest.testAllMethods();
        recordTest.testAllMethods();

        tableTest.clearTestTable();
    }
}
