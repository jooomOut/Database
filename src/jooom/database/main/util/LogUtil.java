package jooom.database.main.util;

import jooom.database.main.dto.TableDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LogUtil {
    private final static Logger log = Logger.getGlobal();

    public static void printTestTitle(String testName, String msg){
        System.out.printf("%-30s - %s\n", testName, msg);
    }

    public static void showTableData(String title, TableDto ret) {
        System.out.println();
        int column = ret.getColumns().length + 1;
        int colLine = column * 20 + column * 3 + 1;

        drawTitle(title, colLine);
        drawLine(colLine);
        drawBox("COLUMN NAME" , ret.getColumns());
        drawLine(colLine);
        drawBox("SIZE" , Arrays.stream(ret.getSizes()).boxed().toArray(Integer[]::new));
        drawLine(colLine);

        String [] primaryCheck = new String[column-1];
        for (int i = 0 ; i < column-1 ; i++) primaryCheck[i] = i == ret.getPrimaryKeyIndex() ? "O" : "X";
        drawBox("PRIMARY KEY" ,  primaryCheck);
        //drawBox("PRIMARY KEY" , colLine, column);
        drawLine(colLine);
    }

    public static void showRecordData(String testName ,String tableName, Map<String, String> record, String[] columns) {
        List<Map<String, String>> records = new ArrayList<>();
        records.add(record);
        showRecordData(testName, tableName, records, columns);
    }

    public static void showRecordData(String testName ,String tableName, List<Map<String, String>> records, String[] columns) {
        System.out.println();
        int column = columns.length;
        int colLine = column * 20 + column * 3 + 1;

        drawTitle(testName, colLine);
        drawTitle(tableName, colLine);
        drawBox(columns);
        for (Map<String, String> record : records){
            String[] columnData = new String[column];
            for (int i = 0 ; i < column; i++){
                if (record.keySet().contains(columns[i])){
                    columnData[i] = record.get(columns[i]);
                }
            }
            drawLine(colLine);
            drawBox(columnData);
        }
        drawLine(colLine);
    }

    private static int getColumnSize(List<Map<String, String>> records) {
         List<Integer> intList = records.stream().map(record -> record.keySet().size()).collect(Collectors.toList());
         return intList.stream().mapToInt(x -> x).max().orElse(0);
    }

    private static void drawBox(String rowName, Object[] data) {
        System.out.printf("| %20s ", rowName);
        for(Object x : data){
            System.out.printf("| %20s ", x);
        }
        System.out.printf("|\n");
    }

    private static void drawBox(Object[] data) {

        for(Object x : data){
            System.out.printf("| %20s ", x);
        }
        System.out.printf("|\n");
    }

    private static void drawTitle(String title, int colLine){
        drawLine(colLine);
        int blank = ((colLine-2) - title.length())/2;
        System.out.printf("|");
        for (int i = 0 ; i < blank-1 ; i++)System.out.printf(" ");
        System.out.printf("%s", title);
        for (int i = 0 ; i < blank ; i++)System.out.printf(" ");
        System.out.printf("|\n");
    }

    private static void drawLine(int col){ for (int i = 0 ; i < col ; i ++)System.out.print("="); System.out.println();}

}
