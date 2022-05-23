package jooom.database.test;

import jooom.database.main.DatabaseInterface;
import jooom.database.main.exception.DuplicateKeyException;
import jooom.database.main.exception.WrongTableDataException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RecordTest {
    private DatabaseInterface databaseInterface;

    public RecordTest(DatabaseInterface databaseInterface) {
        this.databaseInterface = databaseInterface;
    }

    public void testAllMethods(){
        insertRecordTest();
    }

    private void insertRecordTest(){
        String tableName = "createTableTest";
        //for (int i = 0 ; i<400 ; i++)
        insertTest(tableName);
        //insertDuplicateRecord();
        searchTest(tableName);

    }

    private void insertTest(String tableName) {
        insertNormalRecord(tableName);
        insertNormalRecordWithNull(tableName);
        insertDuplicateRecord(tableName);
    }


    private void searchTest(String tableName) {
        Map<String,String> result = null;
        searchRecordByNotNull(tableName, "cc");
        searchRecordWithNull(tableName, "cnull");
        //searchRecordByNotNullBitmap(tableName, "");
    }

    private void searchRecordByNotNull(String tableName, String searchKey) {
        Map<String,String> result = databaseInterface.search(tableName, searchKey);
        if (!result.isEmpty()) { System.out.println("searchRecordByEmptyResult - 테스트 성공");
        } else {
            System.out.println("searchRecordByEmptyResult - 테스트 실패");
        }
    }

    private void searchRecordWithNull(String tableName, String searchKey) {
        Map<String,String> result = databaseInterface.search(tableName, searchKey);
        if (result.isEmpty()) { System.out.println("searchRecordWithNull - 테스트 실패");
        } else {
            System.out.println("searchRecordWithNull - 테스트 성공");
        }
    }

    private void insertNormalRecord(String tableName) {
        Map<String, String> columns = new HashMap<>();
        columns.put("FFF", "FFFf data");
        columns.put("AWE", "awe data");
        columns.put("C", "cc");
        columns.put("D", "dd data");


        try {
            databaseInterface.insert(tableName, columns);
        } catch(WrongTableDataException | DuplicateKeyException e) {
            System.out.println("insertNormalRecord - 테스트 실패");
            return;
        }
        System.out.println("insertNormalRecord - 테스트 성공");
    }

    private void insertNormalRecordWithNull(String tableName) {
        Map<String, String> columns = new HashMap<>();
        columns.put("FFF", "FFFf data");
        columns.put("C", "cnull");
        columns.put("D", "dd data");

        try {
            databaseInterface.insert(tableName, columns);
        } catch(WrongTableDataException | DuplicateKeyException e) {
            System.out.println("insertNormalRecordWithNull - 테스트 실패");
            return;
        }
        System.out.println("insertNormalRecordWithNull - 테스트 성공");
    }

    private void insertDuplicateRecord(String tableName) {
        Map<String, String> columns = new HashMap<>();
        columns.put("FFF", "FFFf data");
        columns.put("C", "cnull");
        columns.put("D", "dd data");

        try {
            databaseInterface.insert(tableName, columns);
        } catch(DuplicateKeyException e) {
            System.out.println("insertDuplicateRecord - 테스트 성공");
            return;
        }
        System.out.println("insertDuplicateRecord - 테스트 성공");
    }
}
