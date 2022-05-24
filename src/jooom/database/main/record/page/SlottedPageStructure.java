package jooom.database.main.record.page;

import jooom.database.main.exception.record.DuplicateKeyException;
import jooom.database.main.exception.record.NoPrimaryKeyException;
import jooom.database.main.record.structure.RecordStructure;
import jooom.database.main.record.structure.VariableLengthRecordStructure;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.*;

public class SlottedPageStructure extends RecordPageStructure {
    private static final String SETTING_PATH = "settings/common.txt";
    private static final String FILE_PATH = "files/records/";

    private static int DEFAULT_SIZE_BYTE = 4096;
    private static int DEFAULT_ENTRY_SIZE_BYTE = 4;
    private static int DEFAULT_END_OF_FREE_SPACE_BYTE = 4;
    private static int DEFAULT_SLOT_OFFSET_SIZE_BYTE = 4;
    private static int DEFAULT_SLOT_SIZE_BYTE = 4;

    public SlottedPageStructure(RecordStructure recordStructure) throws IOException {
        super(recordStructure);
        setPageParameters();
    }

    /* 기본 설정된 레코드 구조 */
    public static SlottedPageStructure of() throws IOException {
        RecordStructure recordStructure = VariableLengthRecordStructure.of();
        return new SlottedPageStructure(recordStructure);
    }

    @Override
    public void insert(String tableName, Map<String, String> columns, String primaryKeyColumnName) {
        validateRecord(tableName, columns, primaryKeyColumnName);
        byte[] recordBytes =  recordStructure.toByteFrom(tableName, columns);
        try {
            insertRecord(tableName, recordBytes);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, String> search(String tableName, String primaryKey) {
        Map<String, String> ret = new HashMap<>();
        int idx = 0;
        while (true) {
            File slottedPage = getFile(tableName , idx);
            if (!slottedPage.exists()) break;
            ret = searchFromSlottedPageByKey(slottedPage, tableName, primaryKey);
            if (!ret.isEmpty()) break;
            idx++;
        }
        return ret;
    }

    @Override
    public List<Map<String, String>> searchColumns(String tableName, String[] columns){
        List<Map<String, String>> ret = new ArrayList<>();
        int idx = 0;
        while (true) {
            File slottedPage = getFile(tableName , idx);
            if (!slottedPage.exists()) break;
            List<Map<String, String>> records = searchColumnsFromPage(slottedPage, tableName, columns);
            if (!records.isEmpty()) ret.addAll(records);
            idx++;
        }
        return ret;
    }

    private void validateRecord(String tableName, Map<String, String> columns, String primaryKeyColumnName) {
        if (!columns.containsKey(primaryKeyColumnName)){
            throw new NoPrimaryKeyException(tableName);
        }
        String primaryKey = columns.get(primaryKeyColumnName);
        if (!search(tableName, primaryKey).isEmpty()){
            throw new DuplicateKeyException(primaryKey);
        }
    }

    private Map<String, String> searchFromSlottedPageByKey(File slottedPage, String tableName, String primaryKey) {
        Map<String, String> ret = new HashMap<>();
        List<byte[]> records = extractRecordByte(slottedPage);
        for (byte[] record : records){
            ret = recordStructure.searchByKey(record, tableName, primaryKey);
            if (!ret.isEmpty()) return ret;
        }
        return new HashMap<>();
    }

    private List<Map<String, String>> searchColumnsFromPage(File slottedPage, String tableName, String[] columns) {
        List<Map<String, String>> ret = new ArrayList<>();
        List<byte[]> records = extractRecordByte(slottedPage);
        for (byte[] record : records){
            Map<String, String> parsedMap = recordStructure.getSpecificColumns(tableName, record, columns);
            if (!parsedMap.isEmpty()) ret.add(parsedMap);
        }
        return ret;
    }

    private List<byte[]> extractRecordByte(File slottedPage){
        List<byte[]> ret = new ArrayList<>();
        try {
            byte[] originFile = Files.readAllBytes(slottedPage.toPath());
            int entrySize = readIntFromByte(originFile, 0, DEFAULT_ENTRY_SIZE_BYTE);

            for (int i = 0 ; i < entrySize ; i++){
                byte[] record = findRecord(originFile, i);
                ret.add(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private byte[] findRecord(byte[] originFile, int entryNum) {
        int offset =  DEFAULT_ENTRY_SIZE_BYTE + DEFAULT_END_OF_FREE_SPACE_BYTE +
                entryNum * (DEFAULT_SLOT_OFFSET_SIZE_BYTE + DEFAULT_SLOT_SIZE_BYTE);
        int recordOffset = readIntFromByte(originFile, offset, DEFAULT_SLOT_OFFSET_SIZE_BYTE);
        int recordSize = readIntFromByte(originFile, offset + DEFAULT_SLOT_OFFSET_SIZE_BYTE, DEFAULT_SLOT_SIZE_BYTE);

        byte[] record = new byte[recordSize];
        System.arraycopy(originFile, recordOffset, record, 0, recordSize);
        return record;
    }

    private void insertRecord(String tableName, byte[] recordBytes) throws IOException {
        int fileNum = findPage(tableName, recordBytes.length);

        File slottedPage = getFile(tableName, fileNum);
        if (!slottedPage.exists()){
            makeDefaultPage(slottedPage);
        }
        addToSlottedPage(slottedPage, recordBytes);
    }

    private File getFile(String tableName, int fileNum){
        File dir = new File(FILE_PATH + "/" + tableName);
        if (!dir.exists()){
            dir.mkdir();
        }
        return new File(FILE_PATH, tableName+"/"+ tableName + "-"+fileNum+".dat");
    }

    private int readIntFromByte(byte[] target, int offset, int size){
        byte[] ret = new byte[size];

        System.arraycopy(target, offset, ret, 0, size);
        return ByteBuffer.allocate(size).put(ret).rewind().getInt();
    }

    private void addToSlottedPage(File slottedPage, byte[] recordBytes) {
        try {
            byte[] originFile = Files.readAllBytes(slottedPage.toPath());

            /*1. 레코드 삽입*/
            int startOfEndOfFreeSpace = DEFAULT_ENTRY_SIZE_BYTE;
            int endOfFreeSize = readIntFromByte(originFile, startOfEndOfFreeSpace, DEFAULT_END_OF_FREE_SPACE_BYTE);

            int startOfRecord = endOfFreeSize - recordBytes.length;

            for (int idx = 0 ; idx < recordBytes.length ; idx++){
                originFile[startOfRecord + idx] = recordBytes[idx];
            }

            /*2. 엔트리 사이즈 갱신*/
            int entrySize = readIntFromByte(originFile, 0, DEFAULT_ENTRY_SIZE_BYTE) + 1;
            byte[] entryBuffer = ByteBuffer.allocate(DEFAULT_ENTRY_SIZE_BYTE).putInt(entrySize).array();
            System.arraycopy(entryBuffer, 0, originFile, 0, DEFAULT_ENTRY_SIZE_BYTE);
            // end of free size 수정 적용
            endOfFreeSize = startOfRecord; // free space 갱신
            byte[] freeSpaceBuffer = ByteBuffer.allocate(DEFAULT_END_OF_FREE_SPACE_BYTE).putInt(endOfFreeSize).array();
            System.arraycopy(freeSpaceBuffer, 0, originFile, DEFAULT_ENTRY_SIZE_BYTE, DEFAULT_END_OF_FREE_SPACE_BYTE);

            int slotOffset = DEFAULT_ENTRY_SIZE_BYTE + DEFAULT_END_OF_FREE_SPACE_BYTE +
                    (entrySize-1) * (DEFAULT_SLOT_OFFSET_SIZE_BYTE + DEFAULT_SLOT_SIZE_BYTE);

            byte[] slotOffsetBuffer = ByteBuffer.allocate(DEFAULT_SLOT_OFFSET_SIZE_BYTE).putInt(startOfRecord).array();
            System.arraycopy(slotOffsetBuffer, 0, originFile, slotOffset + 0, DEFAULT_SLOT_OFFSET_SIZE_BYTE);
            slotOffset += DEFAULT_SLOT_OFFSET_SIZE_BYTE;

            byte[] slotSizeBuffer = ByteBuffer.allocate(DEFAULT_SLOT_SIZE_BYTE).putInt(recordBytes.length).array();
            System.arraycopy(slotSizeBuffer, 0, originFile, slotOffset + 0, DEFAULT_SLOT_SIZE_BYTE);

            FileOutputStream fileOutputStream = new FileOutputStream(slottedPage);
            BufferedOutputStream os = new BufferedOutputStream(fileOutputStream);
            for (byte b : originFile) {
                os.write(b);
            }
            os.flush();
            os.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeDefaultPage(File slottedPage) throws IOException {
        slottedPage.createNewFile();

        FileOutputStream fileOutputStream = new FileOutputStream(slottedPage);
        BufferedOutputStream os = new BufferedOutputStream(fileOutputStream);

        byte[] entry = ByteBuffer.allocate(DEFAULT_ENTRY_SIZE_BYTE).putInt(0).array();
        byte[] freeSize = ByteBuffer.allocate(DEFAULT_END_OF_FREE_SPACE_BYTE).putInt(DEFAULT_SIZE_BYTE).array();
        byte[] result = new byte[DEFAULT_SIZE_BYTE];
        int idx = 0;
        for (byte b : entry) result[idx++] = b;
        for (byte b : freeSize) result[idx++] = b;

        for(byte x : result) os.write(x);

        os.flush();
        os.close();
    }

    private int findPage(String tableName, int length) {
        for (int idx = 0 ; true ; idx++){
            File slottedPage = getFile(tableName, idx);
            if (checkEmptySpace(slottedPage, length)){
                return idx;
            }
        }
    }

    private boolean checkEmptySpace(File slottedPage, int length) {
        if (!slottedPage.exists()) return true;
        try {
            FileInputStream fileInputStream = new FileInputStream(slottedPage);
            BufferedInputStream bs = new BufferedInputStream(fileInputStream);

            byte[] buffer = new byte[DEFAULT_ENTRY_SIZE_BYTE +  DEFAULT_END_OF_FREE_SPACE_BYTE];
            bs.read(buffer, 0, DEFAULT_ENTRY_SIZE_BYTE +  DEFAULT_END_OF_FREE_SPACE_BYTE);

            int entrySize = readIntFromByte(buffer, 0, DEFAULT_ENTRY_SIZE_BYTE);
            int endOfFreeSize = readIntFromByte(buffer, DEFAULT_ENTRY_SIZE_BYTE, DEFAULT_END_OF_FREE_SPACE_BYTE);

            int startEndOfFreeSize = DEFAULT_ENTRY_SIZE_BYTE + DEFAULT_END_OF_FREE_SPACE_BYTE +
                    entrySize * (DEFAULT_SLOT_SIZE_BYTE + DEFAULT_SLOT_OFFSET_SIZE_BYTE);

            if (endOfFreeSize - startEndOfFreeSize < length + DEFAULT_SLOT_SIZE_BYTE + DEFAULT_SLOT_OFFSET_SIZE_BYTE)
                return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }



    private void setPageParameters() throws IOException {
        File settingFile = new File(SETTING_PATH);
        if (!settingFile.exists()){
            // 레코드, 슬롯 페이지에 대한 설정 파일이 없을 경우 기본 값으로 새로 생성한다.
            writeDefaultSetting(settingFile);
        }
        try {
            Scanner sc = new Scanner(settingFile);

            DEFAULT_SIZE_BYTE = Integer.parseInt(sc.nextLine());
            DEFAULT_ENTRY_SIZE_BYTE = Integer.parseInt(sc.nextLine());
            DEFAULT_END_OF_FREE_SPACE_BYTE = Integer.parseInt(sc.nextLine());
            DEFAULT_SLOT_OFFSET_SIZE_BYTE = Integer.parseInt(sc.nextLine());
            DEFAULT_SLOT_SIZE_BYTE = Integer.parseInt(sc.nextLine());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 클래스 생성 시, 설정 txt 파일이 없을 경우
     * 단 1회 시작함. 설정 txt 파일을 기본 값으로 생성
     * */
    private void writeDefaultSetting(File settingFile) throws IOException {
        FileWriter fw = new FileWriter(settingFile);
        PrintWriter writer = new PrintWriter(fw);

        writer.println(DEFAULT_SIZE_BYTE);
        writer.println(DEFAULT_ENTRY_SIZE_BYTE);
        writer.println(DEFAULT_END_OF_FREE_SPACE_BYTE);
        writer.println(DEFAULT_SLOT_OFFSET_SIZE_BYTE);
        writer.println(DEFAULT_SLOT_SIZE_BYTE);
        // 5. BufferedWriter close
        writer.close();
        fw.close();
    }
}


