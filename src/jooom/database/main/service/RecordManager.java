package jooom.database.main.service;

import java.util.LinkedHashMap;
import java.util.Map;

public class RecordManager {
    private TableManager tableManager;

    public RecordManager(TableManager tableManager) {
        this.tableManager = tableManager;
    }

    public void insert(String tableName, Map<String, String> columns) {
        LinkedHashMap<String, String> sortedColumns = tableManager.sortColumns(tableName, columns);

        // TODO : 2. 레코드 파일 구조로 만들기
        // TODO : 3. 페이지 슬롯 구조에 넘기기

    }
}
