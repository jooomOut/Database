package jooom.database.test;

import jooom.database.main.DatabaseInterface;
import jooom.database.main.DatabaseInterfaceImpl;

public class TestManager {
    private DatabaseInterface databaseInterface;
    private TableTest tableTest;
    private RecordTest recordTest;

    public TestManager(DatabaseInterface databaseInterface) {
        this.databaseInterface = databaseInterface;
        this.tableTest = new TableTest(databaseInterface);
        this.recordTest = new RecordTest(databaseInterface);
    }

    public void run(){

        tableTest.testAllMethods();
        recordTest.testAllMethods();
    }
}
