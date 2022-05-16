package jooom.database.main.record.page;

import java.util.Map;

public abstract class RecordPageStructure {

    public abstract void insert(String tableName, Map<String, String> columns);
    public abstract void search(String primaryKey);
    public abstract void searchColumns(String primaryKey, String column);
}
