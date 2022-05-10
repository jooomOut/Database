package jooom.database.service.impl;

import jooom.database.dto.TableDto;
import jooom.database.exception.TableAlreadyExistsException;
import jooom.database.exception.WrongTableDataException;
import jooom.database.service.TableManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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

    private boolean validateTable(TableDto tableDto) {
        if (tableDto.getSizes().length != tableDto.getColumns().length ||
        tableDto.getPrimaryKeyIndex() >= tableDto.getColumns().length){
            return false;
        }
        return true;
    }

    private void makeDictionaryFile(TableDto tableDto) throws IOException {
        File targetFile = new File(DICTIONARY_PATH, tableDto.getTableName() +".txt");
        if (!targetFile.createNewFile()){// 해당 테이블이 이미 존재하면 예외처리
            throw new TableAlreadyExistsException(targetFile.getPath());
        }
        writeDictionaryData(targetFile, tableDto);
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
