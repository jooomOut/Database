package jooom.database.main.service;

import jooom.database.main.record.page.RecordPageStructure;
import jooom.database.main.record.page.SlottedPageStructure;
import jooom.database.main.service.impl.TableManagerImpl;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class RecordManager {
    private static String SETTING_PATH = "settings/common.txt";
    private static String FILE_PATH = "files/records/";

    private TableManager tableManager;
    private RecordPageStructure pageStructure;

    public RecordManager(TableManager tableManager, RecordPageStructure pageStructure) {
        this.tableManager = tableManager;
        this.pageStructure = pageStructure;
    }

    /**
     * Default 생성자
     * */
    public RecordManager() throws IOException {
        this.tableManager = new TableManagerImpl();
        this.pageStructure = SlottedPageStructure.of();
    }

    public void insert(String tableName, Map<String, String> columns) {
        LinkedHashMap<String, String> sortedColumns = tableManager.sortColumns(tableName, columns);
        File dir = new File(FILE_PATH);
        if(!dir.exists()) dir.mkdirs();
        pageStructure.insert(tableName, columns);

    }
}
