package jooom.database;

import jooom.database.dto.TableDto;

import java.util.Map;

public interface dbInterface<T> {
    void createTable(TableDto tableDto);
    void insert(String tableName, Map<String, T> columns);
    Map<String, T> search(String key);
    String[] search(String columnName, String key);
}
