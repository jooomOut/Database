package jooom.application.test;

import jooom.database.main.DatabaseInterface;
import jooom.database.main.DatabaseInterfaceImpl;
import jooom.database.main.DatabaseInterfaceProxy;
import jooom.database.main.dto.TableDto;
import jooom.database.main.exception.record.NoPrimaryKeyException;
import jooom.database.main.exception.table.TableAlreadyExistsException;
import jooom.database.main.exception.table.WrongTableDataException;
import jooom.database.main.record.page.RecordPageStructure;
import jooom.database.main.record.page.SlottedPageStructure;
import jooom.database.main.service.RecordManager;
import jooom.database.main.service.impl.TableManagerImpl;
import jooom.database.main.util.LogUtil;
import jooom.database.test.TestManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    private static DatabaseInterface databaseInterface;
    public static void main(String[] args) throws IOException {
        setDBInterfaceProxy();

        doTest(databaseInterface);
        run(databaseInterface);
    }

    private static void doTest(DatabaseInterface databaseInterface) {
        TestManager testManager = new TestManager(databaseInterface);
        testManager.run();
    }

    private static void run(DatabaseInterface databaseInterface) throws IOException {
        LogUtil.printTitle("TEST END, START DB");
        String command = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (!command.equals("-1")){
            LogUtil.showCommandGuide();
            System.out.printf("원하는 기능을 입력하세요 : ");
            command = br.readLine();
            while (!validateCommand(command)){
                System.out.printf("정해진 범위의 값을 입력하세요 : ");
                command = br.readLine();
            }

            switch (command){
                case "1":
                    createTable(br);
                    break;
                case "2":
                    insertRecord(br);
                    break;
                case "3": break;
                case "4": break;
            }

        }

        //str = br.readLine();


    }

    private static void insertRecord(BufferedReader br) throws IOException {
        Map<String, String> columns = new HashMap<>();
        System.out.printf("테이블 이름을 입력해주세요. :");
        String tableName = br.readLine().strip();

        System.out.printf("컬럼 이름을 입력해주세요 ex) name age weight :");
        String[] columnName = Arrays.stream(br.readLine().split(" ")).map(String::strip).toArray(String[]::new);

        System.out.printf("컬럼 값을 입력해주세요 ex) Bill 20 76 :");
        String[] columnValue = Arrays.stream(br.readLine().split(" ")).map(String::strip).toArray(String[]::new);
        if (columnName.length != columnValue.length){
            System.out.println("잘못된 정보 입니다.");
            return;
        }
        for (int i = 0 ; i < columnName.length ; i++){
            columns.put(columnName[i], columnValue[i]);
        }
        try {
            databaseInterface.insert(tableName, columns);
        } catch (WrongTableDataException e){
            System.err.println("테이블 정보가 잘못되었습니다!");
        } catch (NoPrimaryKeyException e){
            System.err.println("기본 키가 존재하지 않습니다.");
        }
    }

    private static void createTable(BufferedReader br) throws IOException {
        System.out.printf("테이블 이름을 입력해주세요. :");
        String tableName = br.readLine().strip();

        System.out.printf("컬럼 이름을 입력해주세요 ex) name age weight :");
        String[] columns = Arrays.stream(br.readLine().split(" ")).map(String::strip).toArray(String[]::new);

        System.out.printf("컬럼 사이즈를 입력해주세요. 단 가변길이는 0으로 입력해주세요 :");
        int[] sizes = null;
        try {
            sizes = Arrays.stream(br.readLine().split(" ")).mapToInt(Integer::parseInt).toArray();
        } catch (NumberFormatException e){
            System.out.println("잘못된 정보 입니다.");
            return;
        }
        System.out.printf("primary key는 무엇인가요? : ");
        String primaryKey = br.readLine();

        int primaryKeyIdx = IntStream.range(0, columns.length)
                .filter(idx -> columns[idx].equals(primaryKey))
                .findAny()
                .orElse(-1);
        if (!validateTableData(columns, sizes) || primaryKeyIdx == -1){
            System.out.println("잘못된 정보 입니다.");
            return;
        }
        TableDto dto = new TableDto(tableName, columns, sizes, primaryKeyIdx);
        try {
            databaseInterface.createTable(dto);
        } catch (TableAlreadyExistsException e){
            System.err.println("이미 존재하는 테이블 입니다!");
        }
    }

    private static boolean validateTableData(String[] columns, int[] sizes) {
        if (columns.length != sizes.length){
            return false;
        }
        for (int x : sizes){
            if (x < 0) return false;
        }
        return true;
    }

    private static boolean validateCommand(String command) {
        int commandInt = -1;
        try {
            commandInt = Integer.parseInt(command);
        } catch (NumberFormatException e){
            return false;
        }
        if (commandInt <= 0 || commandInt > 4){
            return false;
        }
        return true;
    }

    private static void setDBInterfaceProxy() throws IOException {
        TableManagerImpl tableManager = new TableManagerImpl();
        RecordPageStructure pageStructure = SlottedPageStructure.of();
        RecordManager recordManager = new RecordManager(tableManager, pageStructure);
        DatabaseInterface databaseInterfaceImpl = new DatabaseInterfaceImpl(
                new TableManagerImpl(),
                recordManager
        );
        databaseInterface = new DatabaseInterfaceProxy(databaseInterfaceImpl);
    }

}
