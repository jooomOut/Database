package jooom.application.test;

import jooom.database.main.DatabaseInterface;
import jooom.database.main.DatabaseInterfaceImpl;
import jooom.database.main.record.page.RecordPageStructure;
import jooom.database.main.record.page.SlottedPageStructure;
import jooom.database.main.service.RecordManager;
import jooom.database.main.service.impl.TableManagerImpl;
import jooom.database.test.TestManager;

public class Main {
    private static DatabaseInterface databaseInterface;

    public static void main(String[] args) {
        TableManagerImpl tableManager = new TableManagerImpl();
        RecordPageStructure pageStructure = new SlottedPageStructure();
        RecordManager recordManager = new RecordManager(tableManager, pageStructure);
        databaseInterface = new DatabaseInterfaceImpl(
            new TableManagerImpl(),
                recordManager
        );

        doTest(databaseInterface);
        run(databaseInterface);
    }

    private static void doTest(DatabaseInterface databaseInterface) {
        TestManager testManager = new TestManager(databaseInterface);
        testManager.run();
    }

    private static void run(DatabaseInterface databaseInterface) {

    }

}
