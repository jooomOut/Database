package jooom.database.main.dto;

public class TableDto {
    String tableName;
    String[] columns;
    int[] sizes;
    int primaryKeyIndex;
    String filePath;

    public TableDto(String tableName, String[] columns, int[] sizes, int primaryKeyIndex, String filePath) {
        this.tableName = tableName;
        this.columns = columns;
        this.sizes = sizes;
        this.primaryKeyIndex = primaryKeyIndex;
        this.filePath = filePath;
    }

    public String getTableName() {
        return tableName;
    }

    public String[] getColumns() {
        return columns;
    }

    public int[] getSizes() {
        return sizes;
    }

    public int getPrimaryKeyIndex() {
        return primaryKeyIndex;
    }

    public String getFilePath() {
        return filePath;
    }
}
