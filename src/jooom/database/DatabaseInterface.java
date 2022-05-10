package jooom.database;

import jooom.database.dto.TableDto;

import java.io.IOException;
import java.util.Map;

public interface DatabaseInterface {
    void createTable(TableDto tableDto) throws IOException;
    void insert(String tableName, Map<String, Object> columns);
    Map<String, Object> search(String key);
    String[] search(String columnName, String key);
}
