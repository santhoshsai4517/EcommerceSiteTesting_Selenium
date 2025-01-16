package Utility;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtils {
    private Workbook workbook;
    private Sheet sheet;

    // Constructor to load the Excel file and sheet
    public ExcelUtils(String filePath, String sheetName) {
        try {
            FileInputStream fis = new FileInputStream(new File(filePath));
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheet(sheetName);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load Excel file: " + filePath);
        }
    }

    // Method to read a cell value
    public String readCell(int rowNumber, int columnNumber) {
        Row row = sheet.getRow(rowNumber);
        if (row != null) {
            Cell cell = row.getCell(columnNumber);
            if (cell != null) {
                return cell.toString();
            }
        }
        return null;
    }

    // Method to read multiple rows from the Excel file
    public List<Map<String, String>> readRows(int startRow, int endRow) {
        List<Map<String, String>> rowsData = new ArrayList<>();
        Row headerRow = sheet.getRow(0); // Assuming the first row contains column headers

        if (headerRow == null) {
            throw new RuntimeException("Header row is missing in the sheet.");
        }

        for (int i = startRow; i <= endRow; i++) {
            Row row = sheet.getRow(i);

            if (row != null) {
                Map<String, String> rowData = new LinkedHashMap<>();
                for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                    String header = headerRow.getCell(j).toString();
                    String cellValue = row.getCell(j) != null ? row.getCell(j).toString() : "";
                    rowData.put(header, cellValue);
                }
                rowsData.add(rowData);
            }
        }

//        System.out.println(rowsData);
        return rowsData;
    }

    // Method to write a value to a cell
    public void writeCell(int rowNumber, int columnNumber, String value) {
        Row row = sheet.getRow(rowNumber);
        if (row == null) {
            row = sheet.createRow(rowNumber);
        }
        Cell cell = row.getCell(columnNumber);
        if (cell == null) {
            cell = row.createCell(columnNumber);
        }
        cell.setCellValue(value);
    }

    // Method to get the total number of rows in the sheet
    public int getRowCount() {
        int rowCount = 0;
        for (Row row : sheet) {
            if (row != null && row.getPhysicalNumberOfCells() > 0) {
                rowCount++;
            }
        }
        return rowCount;
    }

    // Method to save changes to the Excel file
    public void save(String filePath) {
        try (FileOutputStream fos = new FileOutputStream(new File(filePath))) {
            workbook.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save Excel file: " + filePath);
        }
    }

    // Method to close the workbook
    public void close() {
        try {
            if (workbook != null) {
                workbook.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
