package jooom.database.main.service;

import jooom.database.main.dto.TableDto;
import jooom.database.main.exception.TableAlreadyExistsException;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public interface TableManager {
    static String FILE_PATH = "files";
    static String DICTIONARY_PATH = FILE_PATH + "/" + "data_dictionary";
    static String RECORD_PATH =  FILE_PATH + "/" + "record";

    public void createTable(TableDto tableDto) throws IOException, TableAlreadyExistsException;
    public LinkedHashMap<String, String> sortColumns(String tableName, Map<String, String> columns);
    public Map<String, Integer> getColumnsSize(String tableName);
}
