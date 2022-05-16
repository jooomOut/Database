package jooom.database.main.record.page;

import jooom.database.main.record.structure.RecordStructure;
import jooom.database.main.record.structure.VariableLengthRecordStructure;

import java.io.*;
import java.util.Map;
import java.util.Scanner;

public class SlottedPageStructure extends RecordPageStructure {
    private static String SETTING_PATH = "settings/common.txt";
    private static int DEFAULT_SIZE_BYTE = 4096;
    private static int DEFAULT_ENTRY_SIZE_BYTE = 4;
    private static int DEFAULT_END_OF_FREE_SPACE_BYTE = 4;
    private static int DEFAULT_SLOT_OFFSET_SIZE_BYTE = 4;
    private static int DEFAULT_SLOT_SIZE_BYTE = 4;

    private RecordStructure recordStructure;

    public SlottedPageStructure(RecordStructure recordStructure) throws IOException {
        super();
        this.recordStructure = recordStructure;
        setPageParameters();
    }

    public SlottedPageStructure() throws IOException {
        /* 기본 설정된 레코드 구조 */
        new SlottedPageStructure(new VariableLengthRecordStructure());
    }

    @Override
    public void insert(String tableName, Map<String, String> columns) {
        //byte[] recordBytes =  recordStructure.toByteFrom(tableName, columns);
        // TODO : 2. 레코드 파일 구조로 만들기
        // TODO : 3. 페이지 슬롯 구조에 넘기기
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
