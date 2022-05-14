package jooom.database.test;

import jooom.database.main.DatabaseInterface;
import jooom.database.main.exception.DuplicateKeyException;
import jooom.database.main.exception.WrongTableDataException;

import java.util.HashMap;
import java.util.Map;

public class RecordTest {
    private DatabaseInterface databaseInterface;

    public RecordTest(DatabaseInterface databaseInterface) {
        this.databaseInterface = databaseInterface;
    }

    public void testAllMethods(){
        insertRecordTest();
    }

    private void insertRecordTest(){
        insertNormalRecord();
        //insertDuplicateRecord();
    }

    private void insertNormalRecord() {
        String tableName = "createTableTest";
        Map<String, String> columns = new HashMap<>();
        columns.put("FFF", "FFFfff data");
        columns.put("AWE", "awe data");
        columns.put("C", "cc data");
        columns.put("D", "dd data");


        try {
            databaseInterface.insert(tableName, columns);
        } catch(WrongTableDataException | DuplicateKeyException e) {
            System.out.println("insertNormalRecord - 테스트 실패");
            return;
        }
        System.out.println("insertNormalRecord - 테스트 성공");
    }
}
