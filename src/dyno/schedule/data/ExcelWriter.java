/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.schedule.data;

import dyno.schedule.models.WorkCenterOpAllocModel;
import dyno.schedule.utils.DateTimeUtil;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author Prabash
 */
public class ExcelWriter
{

    private String xlsxFilePath;

    public ExcelWriter(String filePath)
    {
        xlsxFilePath = filePath;
    }

    public void modifyExistingWorkbook(String sheetName, List<WorkCenterOpAllocModel> workCenterOpAllocs) throws InvalidFormatException, IOException
    {
        // Obtain a workbook from the excel file
        FileInputStream inputStream = new FileInputStream(xlsxFilePath);
        Workbook workbook = WorkbookFactory.create(inputStream);
        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();
        DateFormat dateFormat = DateTimeUtil.getDefaultSimpleDateFormat();

        // Get Sheet at index 0
        Sheet sheet = workbook.getSheet(sheetName);

        for (WorkCenterOpAllocModel workCenterOpAlloc : workCenterOpAllocs)
        {
            for (Row row : sheet)
            {
                try
                {
                    if (dataFormatter.formatCellValue(row.getCell(0)).equals(workCenterOpAlloc.getWorkCenterNo())
                            && dateFormat.parse(dataFormatter.formatCellValue(row.getCell(1))).equals(workCenterOpAlloc.getOperationDate()))
                    {
                        SortedSet<String> keys = new TreeSet<>(workCenterOpAlloc.getTimeBlockAllocation().keySet());

                        for (String key : keys)
                        {
                            int currentOp = workCenterOpAlloc.getTimeBlockAllocation().get(key);
                            if (currentOp != 0)
                            {
                                int index = ExcelManager.getTimeBlockExcelIndex(key);
                                // Get the Cell at index 2 from the above row
                                Cell cell = row.getCell(index);

                                // Create the cell if it doesn't exist
                                if (cell == null)
                                {
                                    
                                    cell = row.createCell(index);
                                }

                                // Update the cell's value
                                cell.setCellValue(currentOp);
                            }
                        }
                    }

                } catch (ParseException ex)
                {
                    Logger.getLogger(ExcelWriter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        inputStream.close();
        // Write the output to the file
        FileOutputStream fileOut = new FileOutputStream(xlsxFilePath);
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();
    }

}
