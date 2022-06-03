package jooom.database.main.record.structure;

import jooom.database.main.dto.TableDto;
import jooom.database.main.exception.record.TooLargeColumnException;
import jooom.database.main.service.TableManager;
import jooom.database.main.service.impl.TableManagerImpl;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class VariableLengthRecordStructure extends RecordStructure{
    private static final String SETTING_PATH = "settings/";
    private static final String FILE_PATH = SETTING_PATH + "record_structure.txt";
    /*BYTE*/
    private static int NULL_BITMAP_SIZE = 2;
    private static int VARIABLE_OFFSET = 4;
    private static int VARIABLE_SIZE = 4;


    public VariableLengthRecordStructure(TableManager tableManager) throws IOException {
        super(tableManager);
        setRecordParameters();
    }
    public static VariableLengthRecordStructure of() throws IOException {
        return new VariableLengthRecordStructure(new TableManagerImpl());
    }

    @Override
    public byte[] toByteFrom(String tableName, Map<String, String> columns) {
        Map<String, Integer> columnSize = tableManager.getColumnsSize(tableName);
        int sizeOfRecord = calcRecordSize(columns, columnSize);
        byte[] ret = new byte[sizeOfRecord];

        setNullBitmap(ret, columns, columnSize);
        setRecord(ret, columns, columnSize);

        return ret;
    }

    @Override
    public Map<String, String> getSpecificColumns(String tableName, byte[] recordByte, String[] columns) {
        TableDto tableDto = tableManager.getTableData(tableName);
        Map<String, String> recordMap = byteToMap(recordByte, tableDto);
        return selectColumns(recordMap, columns, tableName);
    }

    private Map<String, String> selectColumns(Map<String, String> recordMap, String[] columns, String tableName) {
        Map<String, String> ret = new HashMap<>();
        for (String column : columns){
            if (recordMap.containsKey(column)){
                ret.put(column, recordMap.get(column));
            }
        }
        return ret;
    }


    @Override
    public Map<String, String> searchByKey(byte[] record, String tableName, String primaryKey) {
        TableDto tableData = tableManager.getTableData(tableName);
        String primaryColumn = tableData.getColumns()[tableData.getPrimaryKeyIndex()];
        Map<String,String> ret = byteToMap(record, tableData);
        if (ret.containsKey(primaryColumn) && ret.get(primaryColumn).equals(primaryKey)){
            return ret;
        }
        return new HashMap<>();
    }

    private Map<String, String> byteToMap(byte[] record, TableDto tableData) {
        Map<String, String> ret = new HashMap<>();
        short nullBitmap = getNullBitmap(record); int columnNum = tableData.getColumns().length;
        int offset = NULL_BITMAP_SIZE;
        for (int columnIndex = 0 ; columnIndex < columnNum ; columnIndex++){
            if ((nullBitmap & (1 << columnIndex)) > 0){ // null이다.
                continue;
            }
            // null이 아니다. 데이터를 읽으면 됨
            int columnSize = tableData.getSizes()[columnIndex];
            String columnName = tableData.getColumns()[columnIndex];
            if (columnSize > 0){ // 고정 길이
                ret.put(columnName, getStrFromByte(record, offset, columnSize));
                offset += columnSize;
            } else { // 가변 길이
                int variableOffset = getIntFromByte(record, offset, VARIABLE_OFFSET);
                int variableSize = getIntFromByte(record, offset + VARIABLE_OFFSET, VARIABLE_SIZE);
                ret.put(columnName, getStrFromByte(record, variableOffset, variableSize));

                offset += VARIABLE_OFFSET + VARIABLE_SIZE;
            }
        }
        return ret;
    }

    private int getIntFromByte(byte[] record, int offset, int size) {
        byte[] bytes = readByte(record, offset, size);
        return ByteBuffer.allocate(size).put(bytes).rewind().getInt();
    }

    private String getStrFromByte(byte[] record, int offset, int size) {
        byte[] bytes = readByte(record, offset, size);
        return new String(bytes).replaceAll("\0", "");
    }

    private short getNullBitmap(byte[] record) {
        byte[] nullBitMap = readByte(record, 0, NULL_BITMAP_SIZE);
        return ByteBuffer.allocate(NULL_BITMAP_SIZE).put(nullBitMap).rewind().getShort();
    }

    private void setRecord(byte[] ret, Map<String, String> columns, Map<String, Integer> columnSize) {
        LinkedHashMap<String, String> variableMap = new LinkedHashMap<>();
        Map<String, Integer> variablePlace = new HashMap<>();
        int offset = NULL_BITMAP_SIZE;
        for (String key : columns.keySet()){
            int size = columnSize.get(key);
            if (size <= 0){ // 가변길이 레코드
                variableMap.put(key, columns.get(key));
                variablePlace.put(key, offset);
                offset += (VARIABLE_OFFSET + VARIABLE_SIZE);
            } else {
                setFixedRecord(ret, offset , columns.get(key), size);
                offset += size;
            }
        }
        setVariableRecord(ret, offset, variableMap, variablePlace);
    }

    private void setVariableRecord(byte[] ret, int offset, Map<String, String> variableMap, Map<String, Integer> placeMap) {
        for (String key : variableMap.keySet()){
            int offsetPlace = placeMap.get(key);
            String data = variableMap.get(key);

            byte[] offsetByte = ByteBuffer.allocate(VARIABLE_OFFSET).putInt(offset).array();
            for(int i = 0 ; i < VARIABLE_OFFSET ; i++){
                ret[offsetPlace + i] = offsetByte[i];
            }
            offsetPlace += VARIABLE_OFFSET;

            byte[] lengthByte = ByteBuffer.allocate(VARIABLE_OFFSET).putInt(data.length()).array();
            for(int i = 0 ; i < VARIABLE_SIZE ; i++){
                ret[offsetPlace + i] = lengthByte[i];
            }

            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            for(int i = 0 ; i < dataBytes.length ; i++){
                ret[offset + i] = dataBytes[i];
            }
            offset += dataBytes.length;
        }
    }

    private void setFixedRecord(byte[] ret, int offset, String data, int size) {
        byte[] columnBytes = data.getBytes(StandardCharsets.UTF_8);
        if (columnBytes.length > size) {
            throw new TooLargeColumnException(data, size);
        }
        int end = offset + size;
        for (int i = 0 ; i < columnBytes.length ; i++){
            ret[end - columnBytes.length + i] = columnBytes[i];
        }
    }

    // 뒤에서 부터 읽자 그냥..
    private void setNullBitmap(byte[] ret, Map<String, String> columns, Map<String, Integer> fullColumnsSize) {
        int idx = 0; short bit = 0;
        for (String key : fullColumnsSize.keySet()){
            /*키가 없다면 Null*/
            if (!columns.containsKey(key)) bit |= (1 << idx);
            idx++;
        }
        byte[] nullBitmap = ByteBuffer.allocate(NULL_BITMAP_SIZE).putShort(bit).array();
        for(int i = 0 ; i < nullBitmap.length ; i++){
            ret[i] = nullBitmap[i];
        }
    }

    private int calcRecordSize(Map<String, String> columns, Map<String, Integer> columnSize) {
        int ret = 0;
        int slotSize = VARIABLE_OFFSET + VARIABLE_SIZE;
        ret += NULL_BITMAP_SIZE;
        for (String key : columns.keySet()){
            int size = columnSize.get(key);
            ret += size > 0 ? size : columns.get(key).length() + slotSize;
        }
        return ret;
    }

    private byte[] readByte(byte[] origin, int offset, int size){
        byte[] ret = new byte[size];
        for (int idx = 0 ; idx< size; idx++){
            ret[idx] = origin[offset+idx];
        }
        return ret;
    }

    private void setRecordParameters() throws IOException {
        File settingFile = loadFile();
        try {
            Scanner sc = new Scanner(settingFile);

            NULL_BITMAP_SIZE = Integer.parseInt(sc.nextLine());
            VARIABLE_OFFSET = Integer.parseInt(sc.nextLine());
            VARIABLE_SIZE = Integer.parseInt(sc.nextLine());

            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private File loadFile() throws IOException {
        File settingPath = new File(SETTING_PATH);
        if (!settingPath.exists()) settingPath.mkdir();

        File settingFile = new File(FILE_PATH);
        // 레코드 대한 설정 파일이 없을 경우 기본 값으로 새로 생성한다.
        if (!settingFile.exists()) writeDefaultSetting(settingFile);

        return settingFile;
    }

    /**
     * 클래스 생성 시, 설정 txt 파일이 없을 경우
     * 단 1회 시작함. 설정 txt 파일을 기본 값으로 생성
     * */
    private void writeDefaultSetting(File settingFile) throws IOException {
        FileWriter fw = new FileWriter(settingFile);
        PrintWriter writer = new PrintWriter(fw);

        writer.println(NULL_BITMAP_SIZE);
        writer.println(VARIABLE_OFFSET);
        writer.println(VARIABLE_SIZE);

        writer.close();
        fw.close();
    }
}
