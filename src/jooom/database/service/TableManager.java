package jooom.database.service;

import jooom.database.dto.TableDto;
import jooom.database.exception.TableAlreadyExistsException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public interface TableManager{
    static String DICTIONARY_PATH = "data_dictionary";
    static String RECORD_PATH = "record";

    public void createTable(TableDto tableDto) throws IOException, TableAlreadyExistsException;
}
