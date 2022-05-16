package jooom.database.main.record.page;

import java.util.Map;

public abstract class RecordPageStructure {
    protected int SIZE;

    protected int entrySize;
    protected int endOfFreeSpaceSize;
    protected int offsetSize;
    protected int SizeOfSize;

    public RecordPageStructure(int SIZE, int entrySize, int endOfFreeSpaceSize, int offsetSize, int sizeOfSize) {
        this.SIZE = SIZE;
        this.entrySize = entrySize;
        this.endOfFreeSpaceSize = endOfFreeSpaceSize;
        this.offsetSize = offsetSize;
        SizeOfSize = sizeOfSize;
    }

    public abstract void insert(String tableName, Map<String, String> columns);
    public abstract void search(String primaryKey);
    public abstract void searchColumns(String primaryKey, String column);
}
