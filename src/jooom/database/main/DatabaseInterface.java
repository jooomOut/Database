package jooom.database.main;

import jooom.database.main.dto.TableDto;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface DatabaseInterface {
    void createTable(TableDto tableDto) throws IOException;
    void insert(String tableName, Map<String, String> columns);
    Map<String, String> search(String tableName, String key);
    List<Map<String,String>> searchColumns(String tableName, String[] columns);

    void dropTable(String tableName);
    TableDto getTableData(String tableName);
    List<Map<String,String>> findAllRecords(String tableName);
}
