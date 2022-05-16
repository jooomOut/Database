package jooom.database.main.record.structure;

import java.util.Map;

public class VariableLengthRecordStructure extends RecordStructure{

    public VariableLengthRecordStructure() {

    }

    public byte[] toByteFrom(String tableName, Map<String, String> columns) {
        return new byte[]{};
    }
}
