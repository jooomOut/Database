package jooom.database.main.util;

import jooom.database.main.dto.TableDto;

import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Logger;

public class LogUtil {
    private final static Logger log = Logger.getGlobal();

    public static void printTestTitle(String testName, String msg){
        System.out.printf("%-30s - %s\n", testName, msg);
    }

    public static void showTableData(String title, TableDto ret) {
        System.out.println();
        int column = ret.getColumns().length + 1;
        int colLine = column * 20 + column * 3 + 1;
        drawLine(colLine);
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

    private static void drawBox(String rowName, Object[] data) {
        System.out.printf("| %20s ", rowName);
        for(Object x : data){
            System.out.printf("| %20s ", x);
        }
        System.out.printf("|\n");
    }
    private static void drawTitle(String title, int colLine){
        int blank = ((colLine-2) - title.length())/2;
        System.out.printf("|");
        for (int i = 0 ; i < blank-1 ; i++)System.out.printf(" ");
        System.out.printf("%s", title);
        for (int i = 0 ; i < blank ; i++)System.out.printf(" ");
        System.out.printf("|\n");
    }

    private static void drawLine(int col){ for (int i = 0 ; i < col ; i ++)System.out.print("="); System.out.println();}
}
