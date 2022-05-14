package jooom.database.main.service.impl;

import jooom.database.main.dto.TableDto;
import jooom.database.main.exception.TableAlreadyExistsException;
import jooom.database.main.exception.WrongTableDataException;
import jooom.database.main.service.TableManager;


import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class TableManagerImpl implements TableManager {

    /**
     * TableDto 정보대로 테이블을 생성합니다.
     * */
    public void createTable(TableDto tableDto) throws IOException {
        if (!validateTable(tableDto)){
            throw new WrongTableDataException();
        }
        /* 테이블을 만들 디렉토리를 미리 확인*/
        if (!new File(DICTIONARY_PATH).exists()) {
            new File(DICTIONARY_PATH).mkdirs();
        }
        makeDictionaryFile(tableDto);
    }

    public LinkedHashMap<String, String> sortColumns(String tableName, Map<String, String> columns){
        LinkedHashMap<String, String> ret = new LinkedHashMap<>();
        TableDto tableData = loadTableDto(tableName).orElseThrow(WrongTableDataException::new);

        return ret;
    }

    private Optional<TableDto> loadTableDto(String tableName){
        File targetFile = readDictionaryFile(tableName);
        if (!targetFile.exists())
            return Optional.empty();
        return Optional.of(toTableDto(targetFile));
    }

    private TableDto toTableDto(File targetFile) {
        try {
            Scanner sc = new Scanner(targetFile);
            String tableName = sc.nextLine();
            String[] columns = sc.nextLine().split(" ");
            int[] sizes = new int[columns.length];
            for (int i = 0 ; i < sizes.length ; i++){
                sizes[i] = sc.nextInt();
            }
            int primaryKeyIdx = sc.nextInt();
            String filePath = sc.nextLine();
            TableDto ret = new TableDto(
                    tableName,
                    columns,
                    sizes,
                    primaryKeyIdx,
                    filePath
            );
            return ret;
        } catch (FileNotFoundException e) {
            throw new WrongTableDataException();
        }

    }


    private boolean validateTable(TableDto tableDto) {
        if (tableDto.getSizes().length != tableDto.getColumns().length ||
        tableDto.getPrimaryKeyIndex() >= tableDto.getColumns().length){
            return false;
        }
        return true;
    }

    private void makeDictionaryFile(TableDto tableDto) throws IOException {
        File targetFile = readDictionaryFile(tableDto.getTableName());
        if (!targetFile.createNewFile()){// 해당 테이블이 이미 존재하면 예외처리
            throw new TableAlreadyExistsException(targetFile.getPath());
        }
        writeDictionaryData(targetFile, tableDto);
    }

    private File readDictionaryFile(String tableName){
        return  new File(DICTIONARY_PATH, tableName +".txt");
    }
    /**
     * TableDto의 내용을 파일에 쓰는 과정.
     * */
    private void writeDictionaryData(File targetFile, TableDto tableDto) throws IOException {
        FileWriter fw = new FileWriter(targetFile);
        PrintWriter writer = new PrintWriter(fw);

        writer.println(tableDto.getTableName()); // 테이블 이름
        for(String column : tableDto.getColumns()){ // 테이블 컬럼
            writer.print(column + " ");
        }
        writer.println();
        for(int columnSize : tableDto.getSizes()){ // 각 컬럼 별 사이즈
            writer.print(columnSize);
            writer.print(" ");
        }
        writer.println();
        writer.println(tableDto.getPrimaryKeyIndex());
        // TODO: 테이블 레코드 위치 추가하기
        writer.println("real record의 위치!");
        // 5. BufferedWriter close
        writer.close();
    }

}
