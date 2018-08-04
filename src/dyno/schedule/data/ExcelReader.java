/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.schedule.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Prabash
 */
public class ExcelReader
{
    private String xlsxFilePath;

    public ExcelReader(String filePath)
    {
        xlsxFilePath = filePath;
    }

    public Sheet ReadExcelSheet(String sheetName) throws IOException, InvalidFormatException
    {
        // Creating a Workbook from an Excel file (.xls or .xlsx)
        FileInputStream excelFile = new FileInputStream(new File(xlsxFilePath));
        Workbook workbook = new XSSFWorkbook(excelFile);
            
        //Workbook workbook = WorkbookFactory.create(new File(xlsxFilePath));
        // Retrieving the number of sheets in the Workbook
        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

        workbook.forEach(sheet ->
        {
            System.out.println("=> " + sheet.getSheetName());
        });

        // Getting the Sheet at index zero
        Sheet sheet = workbook.getSheet(sheetName);

        // Closing the workbook
        workbook.close();
        
        return sheet;
    }
    
    public void PrintExcelRows(Sheet sheet)
    {
        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();
        
        sheet.forEach(row ->
        {
            if (row.getRowNum() == 0)
                return;
            row.forEach(cell ->
            {
                String cellValue = dataFormatter.formatCellValue(cell);
                System.out.print(cellValue + "\t");
            });
            
            System.out.println();
        });
    }
    
}
