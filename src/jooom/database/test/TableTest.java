package jooom.database.test;

import jooom.database.DatabaseInterface;
import jooom.database.dto.TableDto;
import jooom.database.exception.TableAlreadyExistsException;

import java.util.HashMap;
import java.util.Map;

public class TableTest {
    private DatabaseInterface databaseInterface;

    public TableTest(DatabaseInterface databaseInterface) {
        this.databaseInterface = databaseInterface;
    }

    public void testAllMethods(){
        createTableTest(null);
    }
    public boolean createTableTest(TableDto tableDto){
        // TODO: 테이블 생성 테스트하기
        String tableName = "tableTestName";
        String[] columns = new String[]{"A", "B", "C", "D"};
        int[] size = new int[]{3,0,3};
        int primaryKeyIndex = 1;
        TableDto dto = new TableDto(tableName, columns, size, primaryKeyIndex, null);
        try {
            databaseInterface.createTable(dto);
        } catch(TableAlreadyExistsException e){
            System.out.println("테이블 생성 실패 - " + e.getMessage());
        }

        return true;
    }
}
