package jooom.database.service.impl;

import jooom.database.dto.TableDto;
import jooom.database.exception.TableAlreadyExistsException;
import jooom.database.service.TableManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class TableManagerImpl implements TableManager {

    public void createTable(TableDto tableDto) throws IOException {
        if (!new File(DICTIONARY_PATH).exists()) {
            new File(DICTIONARY_PATH).mkdirs();
        }
        makeDictionaryFile(tableDto);
    }
    private void makeDictionaryFile(TableDto tableDto) throws IOException {
        File targetFile = new File(DICTIONARY_PATH, tableDto.getTableName() +".txt");
        if (!targetFile.createNewFile()){// 테이블이 이미 존재하면 컷
            throw new TableAlreadyExistsException(targetFile.getPath());
        }
        writeDictionaryData(targetFile, tableDto);
    }
    private void writeDictionaryData(File targetFile, TableDto tableDto) throws IOException {
        FileWriter fw = new FileWriter(targetFile);
        PrintWriter writer = new PrintWriter(fw);

        // 4. 파일에 쓰기
        writer.println(tableDto.getTableName());
        for(String column : tableDto.getColumns()){
            writer.print(column);
        }
        writer.println();
        for(int columnSize : tableDto.getSizes()){
            writer.print(columnSize);
        }
        writer.println();
        writer.println(tableDto.getPrimaryKeyIndex());
        writer.println("real record의 위치!");
        // 5. BufferedWriter close
        writer.close();
    }

}
