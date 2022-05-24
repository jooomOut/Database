package jooom.database.main.record.page;

import jooom.database.main.record.structure.RecordStructure;

import java.util.List;
import java.util.Map;

public abstract class RecordPageStructure {

    public RecordPageStructure(RecordStructure recordStructure) {
        this.recordStructure = recordStructure;
    }

    protected RecordStructure recordStructure;
    public abstract void insert(String tableName, Map<String, String> columns, String primaryKey);
    public abstract Map<String, String> search(String tableName, String primaryKey);
    public abstract List<Map<String, String>> searchColumns(String tableName, String[] columns);
}
