package DB_Testing.mariaDBTesting.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import static DB_Testing.mariaDBTesting.utils.ObjectUtil.resultSet;
import static DB_Testing.mariaDBTesting.utils.ObjectUtil.resultSetMetaData;

public class ExcelUtil {



    public static List<String> getColumnValues(String filePath, String sheetName, String columnName) {
        List<String> columnValues = new ArrayList<>();

        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fileInputStream)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet " + sheetName + " not found in file " + filePath);
            }
            int columnIndex = -1;
            Row headerRow = sheet.getRow(0);
            Iterator<Cell> headerCellIterator = headerRow.cellIterator();
            while (headerCellIterator.hasNext()) {
                Cell headerCell = headerCellIterator.next();
                if (columnName.equals(headerCell.getStringCellValue())) {
                    columnIndex = headerCell.getColumnIndex();
                    break;
                }
            }
            if (columnIndex == -1) {
                throw new IllegalArgumentException("Column " + columnName + " not found in sheet " + sheetName);
            }
            Iterator<Row> rowIterator = sheet.rowIterator();
            rowIterator.next(); // skip header row
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell cell = row.getCell(columnIndex);
                if (cell != null) {
                    columnValues.add(cell.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return columnValues;
    }

    public static Map<String, String> getExcelRow(String filePath, String sheetName, String firstCellValue, boolean includeFirstCell) {
        Map<String, String> rowData = new HashMap<>();
        try (FileInputStream file = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(file)) {
            Sheet sheet = workbook.getSheet(sheetName);
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell firstCell = row.getCell(0);
                if (firstCell != null && firstCell.getStringCellValue().equals(firstCellValue)) {
                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        String columnName = cell.getSheet().getRow(0).getCell(cell.getColumnIndex()).getStringCellValue();
                        if (includeFirstCell || cell.getColumnIndex() > 0) {
                            rowData.put(columnName, cell.toString());
                        }
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rowData;
    }



}




