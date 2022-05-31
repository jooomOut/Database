package jooom.database.main.dto;

public class TableDto {
    String tableName;
    String[] columns;
    int[] sizes;
    int primaryKeyIndex;

    public TableDto(String tableName, String[] columns, int[] sizes, int primaryKeyIndex) {
        this.tableName = tableName;
        this.columns = columns;
        this.sizes = sizes;
        this.primaryKeyIndex = primaryKeyIndex;
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

}
