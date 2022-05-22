package jooom.database.main.record.page;

import jooom.database.main.record.structure.RecordStructure;

import java.util.Map;

public abstract class RecordPageStructure {

    public RecordPageStructure(RecordStructure recordStructure) {
        this.recordStructure = recordStructure;
    }

    protected RecordStructure recordStructure;
    public abstract void insert(String tableName, Map<String, String> columns);
    public abstract void search(String primaryKey);
    public abstract void searchColumns(String primaryKey, String column);
}
