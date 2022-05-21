package jooom.database.main.record.structure;

import jooom.database.main.service.TableManager;
import jooom.database.main.service.impl.TableManagerImpl;

import java.io.*;
import java.nio.ByteBuffer;
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
        Map<String, Integer> columnSize = tableManager.getColumnsSize(tableName);
        int sizeOfRecord = calcRecordSize(columns, columnSize);
        byte[] ret = new byte[sizeOfRecord];

        setNullBitmap(ret, tableName, columns, columnSize);
        // TODO: 2022/05/22 순차적으로 레코드 삽입 하는 함수
        
        Arrays.fill(ret, (byte) 1);
        return ret;
    }

    private void setNullBitmap(byte[] ret, String tableName, Map<String, String> columns, Map<String, Integer> fullColumnsSize) {
        int bit = 0; int idx = 15;
        for (String key : fullColumnsSize.keySet()){
            /*키가 없다면 Null*/
            if (!columns.containsKey(key)){
                bit |= (1 << idx);
            }
            idx--;
        }
        byte[] nullBitmap = ByteBuffer.allocate(NULL_BITMAP_SIZE).putInt(bit).array();
        for(int i = 0 ; i < nullBitmap.length ; i++){
            ret[i] = nullBitmap[i];
        }
    }

    private int calcRecordSize(Map<String, String> columns, Map<String, Integer> columnSize) {
        int ret = 0;
        ret += NULL_BITMAP_SIZE;
        for (String key : columns.keySet()){
            int size = columnSize.get(key);
            ret += size > 0 ? size : columns.get(key).length();
        }
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
