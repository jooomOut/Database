package jooom.database.main.service;

import jooom.database.main.record.page.RecordPageStructure;
import jooom.database.main.record.page.SlottedPageStructure;
import jooom.database.main.service.impl.TableManagerImpl;

import java.util.LinkedHashMap;
import java.util.Map;

public class RecordManager {
    private TableManager tableManager;
    private RecordPageStructure pageStructure;

    public RecordManager(TableManager tableManager, RecordPageStructure pageStructure) {
        this.tableManager = tableManager;
        this.pageStructure = pageStructure;
    }

    /**
     * Default 생성자
     * */
    public RecordManager() {
        this.tableManager = new TableManagerImpl();
        this.pageStructure = new SlottedPageStructure();
    }

    public void insert(String tableName, Map<String, String> columns) {
        LinkedHashMap<String, String> sortedColumns = tableManager.sortColumns(tableName, columns);
        pageStructure.insert(tableName, columns);

    }
}
