package jooom.database.main.record.structure;

import jooom.database.main.service.TableManager;
import jooom.database.main.service.impl.TableManagerImpl;

import java.io.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

public class VariableLengthRecordStructure extends RecordStructure{
    private static final String SETTING_PATH = "settings/record_structure.txt";

    private static int NULL_BITMAP_SIZE = 2;

    private TableManager tableManager;
    public VariableLengthRecordStructure(TableManager tableManager) throws IOException {
        setRecordParameters();
    }
    public static VariableLengthRecordStructure of() throws IOException {
        return new VariableLengthRecordStructure(new TableManagerImpl());
    }

    public byte[] toByteFrom(String tableName, Map<String, String> columns) {
        byte[] ret = new byte[100];
        Arrays.fill(ret, (byte) 1);
        return ret;
    }

    private void setRecordParameters() throws IOException {
        File settingFile = new File(SETTING_PATH);
        if (!settingFile.exists()){
            // 레코드 대한 설정 파일이 없을 경우 기본 값으로 새로 생성한다.
            writeDefaultSetting(settingFile);
        }
        try {
            Scanner sc = new Scanner(settingFile);

            NULL_BITMAP_SIZE = Integer.parseInt(sc.nextLine());
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

        writer.println(NULL_BITMAP_SIZE);

        writer.close();
    }
}
