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
        LogUtil.showTableData("CREATE_TABLE - "+tableDto.getTableName(),tableDto);
        target.createTable(tableDto);
        getTableData(tableDto.getTableName());
    }

    @Override
    public void insert(String tableName, Map<String, String> columns) {
        target.insert(tableName, columns);
    }

    @Override
    public Map<String, String> search(String tableName, String key) {
        Map<String, String> ret = target.search(tableName, key);
        return ret;
    }

    @Override
    public List<Map<String, String>> searchColumns(String tableName, String[] columns) {
        List<Map<String, String>> ret = target.searchColumns(tableName, columns);
        return ret;
    }

    @Override
    public void dropTable(String tableName) {
        target.dropTable(tableName);
    }

    @Override
    public TableDto getTableData(String tableName) {
        TableDto ret = target.getTableData(tableName);
        LogUtil.showTableData(tableName, ret);
        return ret;
    }
}
