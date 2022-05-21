package jooom.database.main.record.page;

import jooom.database.main.record.structure.RecordStructure;
import jooom.database.main.record.structure.VariableLengthRecordStructure;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

public class SlottedPageStructure extends RecordPageStructure {
    private static String SETTING_PATH = "settings/common.txt";
    private static String FILE_PATH = "files/records/";

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
        RecordStructure recordStructure = new VariableLengthRecordStructure();
        return new SlottedPageStructure(recordStructure);
    }

    @Override
    public void insert(String tableName, Map<String, String> columns) {
        byte[] recordBytes =  recordStructure.toByteFrom(tableName, columns);
        try {
            insertRecord(tableName, recordBytes);
        } catch(IOException e){
            // TODO: 예외처리
            e.printStackTrace();
        }
    }

    private void insertRecord(String tableName, byte[] recordBytes) throws IOException {
        int fileNum = findPage(tableName, recordBytes.length);

        File slottedPage = new File(FILE_PATH, tableName + fileNum +".txt");
        if (!slottedPage.exists()){
            makeDefaultPage(slottedPage);
        }
        // TODO: 2022/05/21 데이터 집어넣기

    }

    private void makeDefaultPage(File slottedPage) throws IOException {
        slottedPage.createNewFile();

        FileOutputStream fileOutputStream = new FileOutputStream(slottedPage);
        BufferedOutputStream os = new BufferedOutputStream(fileOutputStream);

        byte[] entry = ByteBuffer.allocate(DEFAULT_ENTRY_SIZE_BYTE).putInt(0).array();
        byte[] freeSize = ByteBuffer.allocate(DEFAULT_END_OF_FREE_SPACE_BYTE).putInt(DEFAULT_SIZE_BYTE).array();

        for (int i = 0; i < entry.length; i++) {
            os.write(entry[i]); // 버퍼의 모든 문자 쓰기
        }
        for (int i = 0; i < freeSize.length; i++) {
            os.write(freeSize[i]); // 버퍼의 모든 문자 쓰기
        }

        os.flush();
        os.close();

    }

    private int findPage(String tableName, int length) {
        for (int idx = 0 ; true ; idx++){
            File slottedPage = new File(FILE_PATH, tableName + idx +".txt");
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

            byte[] buffer = new byte[Math.max(DEFAULT_ENTRY_SIZE_BYTE, DEFAULT_END_OF_FREE_SPACE_BYTE)];

            bs.read(buffer, 0, DEFAULT_ENTRY_SIZE_BYTE);
            int entrySize = ByteBuffer.allocate(DEFAULT_END_OF_FREE_SPACE_BYTE).put(buffer).rewind().getInt();

            Arrays.fill(buffer, (byte)0);
            bs.read(buffer, 0, DEFAULT_ENTRY_SIZE_BYTE);
            int endOfFreeSize = ByteBuffer.allocate(DEFAULT_END_OF_FREE_SPACE_BYTE).put(buffer).rewind().getInt();

            int startEndOfFreeSize = DEFAULT_ENTRY_SIZE_BYTE + DEFAULT_END_OF_FREE_SPACE_BYTE +
                    entrySize * (DEFAULT_SLOT_SIZE_BYTE + DEFAULT_SLOT_OFFSET_SIZE_BYTE);

            if (endOfFreeSize - startEndOfFreeSize < length)
                return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void search(String primaryKey) {

    }

    @Override
    public void searchColumns(String primaryKey, String column) {

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
    }
}


