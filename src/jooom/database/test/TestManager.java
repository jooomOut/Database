package jooom.database.test;

import jooom.database.DatabaseInterface;
import jooom.database.DatabaseInterfaceImpl;

public class TestManager {
    private DatabaseInterface databaseInterface = new DatabaseInterfaceImpl();
    private TableTest tableTest;
    public TestManager() {
        this.tableTest = new TableTest(databaseInterface);

    }

    public void run(){
        tableTest.testAllMethods();
    }
}
