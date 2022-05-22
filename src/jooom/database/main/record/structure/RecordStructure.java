package jooom.database.main.record.structure;

import jooom.database.main.service.TableManager;

import java.util.Map;

abstract public class RecordStructure {
    protected final TableManager tableManager;
    public RecordStructure(TableManager tableManager) {
        this.tableManager = tableManager;
    }

    abstract public byte[] toByteFrom(String tableName, Map<String, String> columns);
}
