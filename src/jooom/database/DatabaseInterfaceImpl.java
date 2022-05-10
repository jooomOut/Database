package jooom.database;

import jooom.database.dto.TableDto;
import jooom.database.service.TableManager;
import jooom.database.service.impl.TableManagerImpl;

import java.io.IOException;
import java.util.Map;

public class DatabaseInterfaceImpl implements DatabaseInterface {
    private TableManager tableManager = new TableManagerImpl();
    @Override
    public void createTable(TableDto tableDto) throws IOException{
        tableManager.createTable(tableDto);
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