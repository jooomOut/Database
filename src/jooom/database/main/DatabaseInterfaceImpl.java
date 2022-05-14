package jooom.database.main;

import jooom.database.main.dto.TableDto;
import jooom.database.main.service.RecordManager;
import jooom.database.main.service.TableManager;
import jooom.database.main.service.impl.TableManagerImpl;


import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class DatabaseInterfaceImpl implements DatabaseInterface {
    private TableManager tableManager = new TableManagerImpl();
    private RecordManager recordManager = new RecordManager();
    @Override
    public void createTable(TableDto tableDto) throws IOException{
        tableManager.createTable(tableDto);
    }

    @Override
    public Map<String, String> search(String key) {
        return null;
    }

    @Override
    public String[] search(String columnName, String key) {
        return new String[0];
    }

    @Override
    public void insert(String tableName, Map<String, String> columns) {
        LinkedHashMap<String, String> sortedColumns = tableManager.sortColumns(tableName, columns);
        // todo: 1. 테이블 데이터 읽어서 순서 맞추기
        // TODO : 2. 데이터 추가하기
        // TODO : 3. 데이터 추가하기
        // TODO : 2. 데이터 추가하기

    }
}
