package jooom.database.main;

import jooom.database.main.dto.TableDto;
import jooom.database.main.service.RecordManager;
import jooom.database.main.service.TableManager;
import jooom.database.main.service.impl.TableManagerImpl;

import java.io.IOException;
import java.util.Map;

public class DatabaseInterfaceImpl implements DatabaseInterface {
    private TableManager tableManager;
    private RecordManager recordManager;

    public DatabaseInterfaceImpl(TableManager tableManager, RecordManager recordManager) {
        this.tableManager = tableManager;
        this.recordManager = recordManager;
    }

    public DatabaseInterfaceImpl() throws IOException {
        this.tableManager = new TableManagerImpl();
        this.recordManager = new RecordManager();
    }

    @Override
    public void createTable(TableDto tableDto) throws IOException{
        tableManager.createTable(tableDto);
    }

    @Override
    public Map<String, String> search(String tableName, String key) {
        return recordManager.search(tableName, key);
    }

    @Override
    public String[] search(String tableName, String columnName, String key) {
        return new String[0];
    }

    @Override
    public void insert(String tableName, Map<String, String> columns) {
        recordManager.insert(tableName, columns);
    }
}
