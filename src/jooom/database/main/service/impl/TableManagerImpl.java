package jooom.database.main.service.impl;

import jooom.database.main.dto.TableDto;
import jooom.database.main.exception.table.TableAlreadyExistsException;
import jooom.database.main.exception.table.WrongTableDataException;
import jooom.database.main.service.TableManager;


import java.io.*;
import java.util.*;

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

    @Override
    public void dropTable(String tableName) throws WrongTableDataException {
        File tableFile = readDictionaryFile(tableName);
        if (!tableFile.exists()) throw new WrongTableDataException();
        tableFile.delete();
    }

    @Override
    public LinkedHashMap<String, String> sortColumns(String tableName, Map<String, String> columns){
        LinkedHashMap<String, String> ret = new LinkedHashMap<>();
        TableDto tableData = loadTableDto(tableName).orElseThrow(WrongTableDataException::new);
        for (String column : tableData.getColumns()){
            if (columns.containsKey(column)){
                ret.put(column, columns.get(column));
            }
        }
        return ret;
    }

    @Override
    public Map<String, Integer> getColumnsSize(String tableName){
        LinkedHashMap<String, Integer> ret = new LinkedHashMap<>();
        TableDto tableData = loadTableDto(tableName).orElseThrow(WrongTableDataException::new);
        for (int i = 0 ; i < tableData.getColumns().length ; i++){
            ret.put(tableData.getColumns()[i], tableData.getSizes()[i]);
        }
        return ret;
    }

    @Override
    public TableDto getTableData(String tableName) {
        return loadTableDto(tableName).orElseThrow(WrongTableDataException::new);
    }

    private Optional<TableDto> loadTableDto(String tableName){
        File targetFile = readDictionaryFile(tableName);
        if (!targetFile.exists())
            return Optional.empty();
        return Optional.of(toTableDto(targetFile));
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
        writer.println(RECORD_PATH +"/" +tableDto.getTableName()+"/");
        // 5. BufferedWriter close
        writer.close();
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
            TableDto ret = new TableDto(
                    tableName,
                    columns,
                    sizes,
                    primaryKeyIdx
            );
            return ret;
        } catch (FileNotFoundException e) {
            throw new WrongTableDataException();
        }

    }


}
