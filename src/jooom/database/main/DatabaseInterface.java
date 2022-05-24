package jooom.database.main;

import jooom.database.main.dto.TableDto;

import java.io.IOException;
import java.util.Map;

public interface DatabaseInterface {
    void createTable(TableDto tableDto) throws IOException;
    void insert(String tableName, Map<String, String> columns);
    Map<String, String> search(String tableName, String key);
    String[] search(String tableName, String columnName, String key);

    void dropTable(String tableName);
}
