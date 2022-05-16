package jooom.database.main.recordpage;

import java.util.Map;

public class SlottedPageStructure extends RecordPageStructure {
    private static final int SIZE_BYTE = 8192;
    private static final int ENTRY_SIZE_BYTE = 4;
    private static final int END_OF_FREE_SPACE_BYTE = 4;
    private static final int SLOT_OFFSET_SIZE_BYTE = 4;
    private static final int SLOT_SIZE_BYTE = 4;


    public SlottedPageStructure() {
        super(SIZE_BYTE, ENTRY_SIZE_BYTE, END_OF_FREE_SPACE_BYTE, SLOT_OFFSET_SIZE_BYTE, SLOT_SIZE_BYTE);
    }

    public SlottedPageStructure(int SIZE, int entrySize, int endOfFreeSpaceSize, int offsetSize, int sizeOfSize) {
        super(SIZE, entrySize, endOfFreeSpaceSize, offsetSize, sizeOfSize);
    }

    @Override
    public void insert(String tableName, Map<String, String> columns) {

    }

    @Override
    public void search(String primaryKey) {

    }

    @Override
    public void searchColumns(String primaryKey, String column) {

    }
}
