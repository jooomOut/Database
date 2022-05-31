package jooom.database.test;

import jooom.database.main.DatabaseInterface;
import jooom.database.main.dto.TableDto;
import jooom.database.main.exception.record.DuplicateKeyException;
import jooom.database.main.exception.table.WrongTableDataException;
import jooom.database.main.util.LogUtil;
import jooom.database.test.exception.TestFailException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordTest {
    private DatabaseInterface databaseInterface;
    private int recordNum = 0;
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
        // 단일 검색
        searchRecordByNotNull(tableName, "20173156");
        searchRecordWithNull(tableName, "20175382");
        // 컬럼 겅색
        searchRecords(tableName);
    }

    private void insertNormalRecord(String tableName) {
        String testName = "insertNormalRecord";
        Map<String, String> columns = new HashMap<>();
        columns.put("id", "20173156");
        columns.put("tot_cred", "132");
        columns.put("dept_name", "industrial security");
        columns.put("name", "kim jun ki");

        try {
            databaseInterface.insert(tableName, columns);

        } catch(WrongTableDataException | DuplicateKeyException e) {
            LogUtil.printTestTitle(testName, "테스트 실패");
            throw new TestFailException(testName);
        }
        LogUtil.printTestTitle(testName, "테스트 성공");
        recordNum++;
    }

    private void insertNormalRecordWithNull(String tableName) {
        String testName = "insertNormalRecordWithNull";
        Map<String, String> columns = new HashMap<>();
        columns.put("id", "20175382");
        columns.put("tot_cred", "132");
        columns.put("name", "JJJ");

        try {
            databaseInterface.insert(tableName, columns);
        } catch(WrongTableDataException | DuplicateKeyException e) {
            LogUtil.printTestTitle(testName, "테스트 실패");
            throw new TestFailException(testName);
        }
        LogUtil.printTestTitle(testName, "테스트 성공");
        recordNum++;
    }

    private void insertDuplicateRecord(String tableName) {
        String testName= "insertDuplicateRecord";
        Map<String, String> columns = new HashMap<>();
        columns.put("id", "20173156");
        columns.put("tot_cred", "132");
        columns.put("dept_name", "industrial security");
        columns.put("name", "kim jun ki");

        try {
            databaseInterface.insert(tableName, columns);
        } catch(DuplicateKeyException e) {
            databaseInterface.findAllRecords(tableName);
            LogUtil.printTestTitle(testName, "테스트 성공");
            return;
        }
        LogUtil.printTestTitle(testName, "테스트 실패");
        throw new TestFailException(testName);
    }

    private void searchRecordByNotNull(String tableName, String searchKey) {
        String testName = "searchRecordByNotNull";
        Map<String,String> result = databaseInterface.search(tableName, searchKey);
        if (!result.isEmpty()) { LogUtil.printTestTitle(testName, "테스트 성공");
        } else {
            LogUtil.printTestTitle(testName, "테스트 실패");
            throw new TestFailException(testName);
        }
    }

    private void searchRecordWithNull(String tableName, String searchKey) {
        String testName= "searchRecordWithNull";
        Map<String,String> result = databaseInterface.search(tableName, searchKey);
        if (result.isEmpty()) {
            LogUtil.printTestTitle(testName, "테스트 실패");
            throw new TestFailException(testName);
        } else {
            LogUtil.printTestTitle(testName, "테스트 성공");
        }
    }


    private void searchRecords(String tableName) {
        String[] columns = new String[]{"id", "tot_cred"};
        List<Map<String,String>> ret = databaseInterface.searchColumns(tableName, columns);

        if(recordNum == ret.size()){
            LogUtil.printTestTitle("searchRecords", "테스트 성공");
        } else {
            LogUtil.printTestTitle("searchRecords", "테스트 실패");
            throw new TestFailException("searchRecords");
        }
    }

}
