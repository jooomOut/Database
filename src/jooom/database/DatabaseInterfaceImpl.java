package jooom.database;

import jooom.database.dto.TableDto;

import java.util.Map;

public class DatabaseInterfaceImpl implements DatabaseInterface {
    @Override
    public void createTable(TableDto tableDto) {

    }

    @Override
    public Map<String, Object> search(String key) {
        return null;
    }

    @Override
    public String[] search(String columnName, String key) {
        return new String[0];
    }

    @Override
    public void insert(String tableName, Map<String, Object> columns) {

    }
}
