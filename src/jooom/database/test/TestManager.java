package jooom.database.test;

import jooom.database.main.DatabaseInterface;
import jooom.database.main.DatabaseInterfaceImpl;

public class TestManager {
    private DatabaseInterface databaseInterface = new DatabaseInterfaceImpl();
    private TableTest tableTest;
    private RecordTest recordTest;
    public TestManager() {
        this.tableTest = new TableTest(databaseInterface);
        this.recordTest = new RecordTest(databaseInterface);
    }

    public void run(){

        tableTest.testAllMethods();
        recordTest.testAllMethods();
    }
}
