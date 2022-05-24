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
        String tableName = "student";
        //for (int i = 0 ; i<400 ; i++)
        insertTest(tableName);
        searchTest(tableName);

    }

    private void insertTest(String tableName) {
        insertNormalRecord(tableName);
        insertNormalRecordWithNull(tableName);
        insertDuplicateRecord(tableName);
    }


    private void searchTest(String tableName) {
        Map<String,String> result = null;
        searchRecordByNotNull(tableName, "20173156");
        searchRecordWithNull(tableName, "20175382");
        //searchRecordByNotNullBitmap(tableName, "");
        searchRecords(tableName);
    }

        private void insertNormalRecord(String tableName) {
        Map<String, String> columns = new HashMap<>();
        columns.put("id", "20173156");
        columns.put("tot_cred", "132");
        columns.put("dept_name", "industrial security");
        columns.put("name", "kim jun ki");

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
        columns.put("id", "20175382");
        columns.put("tot_cred", "132");
        columns.put("name", "JJJ");

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
        columns.put("id", "20173156");
        columns.put("tot_cred", "132");
        columns.put("dept_name", "industrial security");
        columns.put("name", "kim jun ki");

        try {
            databaseInterface.insert(tableName, columns);
        } catch(DuplicateKeyException e) {
            System.out.println("insertDuplicateRecord - 테스트 성공");
            return;
        }
        System.out.println("insertDuplicateRecord - 테스트 성공");
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


    private void searchRecords(String tableName) {

    }

}
