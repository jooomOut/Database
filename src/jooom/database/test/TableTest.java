package jooom.database.test;

import jooom.database.main.DatabaseInterface;
import jooom.database.main.dto.TableDto;
import jooom.database.main.exception.table.TableAlreadyExistsException;
import jooom.database.main.util.LogUtil;

import java.io.IOException;

public class TableTest {
    private DatabaseInterface databaseInterface;

    private static final String tableName = "student";
    private static final String[] columns = new String[]{"dept_name", "name", "id", "tot_cred"};
    private static final int[] size = new int[]{0,0,11,3};
    private static final int primaryKeyIndex = 2;
    public TableTest(DatabaseInterface databaseInterface) {
        this.databaseInterface = databaseInterface;
    }

    public void testAllMethods(){
        createTableTest();
    }
    public void clearTestTable() {
        databaseInterface.dropTable(tableName);
    }
    public void createTableTest(){
        createNormalTable();
        createDuplicateTable();
    }

    private void createDuplicateTable() {
        String beforeTest = "createDuplicateTable - 입력 값";
        String afterTest = "createDuplicateTable - 결과 값";
        String testName = "createDuplicateTable";
        TableDto dto = new TableDto(tableName, columns, size, primaryKeyIndex, null);
        LogUtil.showTableData(beforeTest,dto);
        try {
            databaseInterface.createTable(dto);
        } catch(TableAlreadyExistsException e){
            TableDto ret = databaseInterface.getTableData(tableName);
            LogUtil.showTableData(afterTest,ret);
            LogUtil.printTestTitle(testName, "테스트 성공");
        } catch(IOException e){
            LogUtil.printTestTitle(testName, "테스트 실패");
        }

    }

    private void createNormalTable() {
        String beforeTest = "createNormalTable - 입력 값";
        String afterTest = "createNormalTable - 결과";
        String testName=  "createNormalTable";
        TableDto dto = new TableDto(tableName, columns, size, primaryKeyIndex, null);
        LogUtil.showTableData(beforeTest,dto);
        try {
            databaseInterface.createTable(dto);
            TableDto ret = databaseInterface.getTableData(tableName);
            LogUtil.showTableData(afterTest, ret);
            LogUtil.printTestTitle(testName, "테스트 성공");
        } catch(Exception e){
            LogUtil.printTestTitle(testName, "테스트 실패");
        }
    }
}
