package jooom.database.main.record.structure;

import java.util.Map;

abstract public class RecordStructure {

    public RecordStructure() {

    }

    abstract public byte[] toByteFrom(String tableName, Map<String, String> columns);
}
