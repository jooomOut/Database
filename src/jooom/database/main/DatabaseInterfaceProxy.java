package jooom.database.main;

import jooom.database.main.dto.TableDto;
import jooom.database.main.util.LogUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DatabaseInterfaceProxy implements DatabaseInterface {
    private final DatabaseInterface target;

    public DatabaseInterfaceProxy(DatabaseInterface target) {
        this.target = target;
    }

    @Override
    public void createTable(TableDto tableDto) throws IOException {
        /*입력 값 출력*/
        LogUtil.showTableData("CREATE_TABLE - "+tableDto.getTableName(),tableDto);

        target.createTable(tableDto);
        /*테이블 생성 검증*/
        getTableData(tableDto.getTableName());
    }

    @Override
    public void insert(String tableName, Map<String, String> columns) {
        /*입력 값 출력*/
        TableDto tableDto = target.getTableData(tableName);
        LogUtil.showRecordData("INSERT_RECORD", tableName, columns, tableDto.getColumns());

        target.insert(tableName, columns);
        /*레코드 추가 검증*/
        findAllRecords(tableName);
    }

    @Override
    public Map<String, String> search(String tableName, String key) {
        Map<String, String> ret = target.search(tableName, key);
        /*레코드 출력*/
        LogUtil.showRecordData("SEARCH BY KEY - "+key, tableName, ret, target.getTableData(tableName).getColumns());
        return ret;
    }

    @Override
    public List<Map<String, String>> searchColumns(String tableName, String[] columns) {
        List<Map<String, String>> ret = target.searchColumns(tableName, columns);
        /*레코드 출력*/
        LogUtil.showRecordData(tableName, tableName, ret, columns);
        return ret;
    }

    @Override
    public void dropTable(String tableName) {
        target.dropTable(tableName);
    }

    @Override
    public TableDto getTableData(String tableName) {
        TableDto ret = target.getTableData(tableName);
        /*레코드 출력*/
        LogUtil.showTableData(tableName, ret);
        return ret;
    }

    @Override
    public List<Map<String, String>> findAllRecords(String tableName) {
        List<Map<String, String>> ret =  target.findAllRecords(tableName);
        /*레코드 출력*/
        TableDto tableDto = target.getTableData(tableName);
        LogUtil.showRecordData("FIND_ALL_RECORD", tableName, ret, tableDto.getColumns());
        return ret;
    }
}
