/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.schedule.data;

import dyno.schedule.utils.DateTimeUtil;
import dyno.schedule.enums.OperationStatus;
import dyno.schedule.enums.ShopOrderPriority;
import dyno.schedule.enums.ShopOrderScheduleStatus;
import dyno.schedule.enums.ShopOrderSchedulingDirection;
import dyno.schedule.enums.ShopOrderStatus;
import dyno.schedule.models.ShopOrderModel;
import dyno.schedule.models.ShopOrderOperationModel;
import dyno.schedule.models.WorkCenterModel;
import dyno.schedule.models.WorkCenterOpAllocModel;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author Prabash
 */
public class ExcelManager
{

    public static final String EXCEL_FILE = "data.xlsx";

    public <T> List<T> getExcelData(String sheetName)
    {
        try
        {
            Sheet excelSheet = (new ExcelReader(EXCEL_FILE)).ReadExcelSheet(sheetName);

            switch (sheetName)
            {
                case "ShopOrders":
                {
                    return (List<T>) getShopOrderData(excelSheet);
                }
                case "ShopOrderOperations":
                {
                    return (List<T>) getShopOrderOperationData(excelSheet);
                }
                case "WorkCenters":
                {
                    return (List<T>) getWorkCenterData(excelSheet);
                }
                case "WorkCenterOpAllocations":
                {
                    return (List<T>) getWorkCenterOpAllocations(excelSheet);
                }

            }
        } catch (IOException ex)
        {
            Logger.getLogger(ExcelManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException ex)
        {
            Logger.getLogger(ExcelManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex)
        {
            Logger.getLogger(ExcelManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private List<ShopOrderModel> getShopOrderData(Sheet sheet) throws ParseException
    {
        List<ShopOrderModel> shopOrders = new ArrayList<>();
        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();

        for (Row row : sheet)
        {
            //skip the header row of the excel
            if (row.getRowNum() == 0)
            {
                continue;
            }
            DateFormat dateFormat = DateTimeUtil.getDefaultSimpleDateFormat();
            ShopOrderModel shopOrder = new ShopOrderModel();

            int i = -1;

            shopOrder.setOrderNo(dataFormatter.formatCellValue(row.getCell(++i)));
            shopOrder.setDescription(dataFormatter.formatCellValue(row.getCell(++i)));
            shopOrder.setCreatedDate(row.getCell(++i) == null ? null : dateFormat.parse(dataFormatter.formatCellValue(row.getCell(i))));
            shopOrder.setPartNo(dataFormatter.formatCellValue(row.getCell(++i)));
            shopOrder.setStructureRevision(dataFormatter.formatCellValue(row.getCell(++i)));
            shopOrder.setRoutingRevision(dataFormatter.formatCellValue(row.getCell(++i)));
            shopOrder.setRequiredDate(row.getCell(++i) == null ? null : dateFormat.parse(dataFormatter.formatCellValue(row.getCell(i))));
            shopOrder.setStartDate(row.getCell(++i) == null ? null : dateFormat.parse(dataFormatter.formatCellValue(row.getCell(i))));
            shopOrder.setFinishDate(row.getCell(++i) == null ? null : dateFormat.parse(dataFormatter.formatCellValue(row.getCell(i))));
            shopOrder.setSchedulingDirection(ShopOrderSchedulingDirection.valueOf(dataFormatter.formatCellValue(row.getCell(++i))));
            shopOrder.setCustomerNo(dataFormatter.formatCellValue(row.getCell(++i)));
            shopOrder.setSchedulingStatus(ShopOrderScheduleStatus.valueOf(dataFormatter.formatCellValue(row.getCell(++i))));
            shopOrder.setShopOrderStatus(ShopOrderStatus.valueOf(dataFormatter.formatCellValue(row.getCell(++i))));
            shopOrder.setPriority(ShopOrderPriority.valueOf(dataFormatter.formatCellValue(row.getCell(++i))));

            shopOrders.add(shopOrder);
            System.out.println();
        }
        return shopOrders;
    }

    private List<ShopOrderOperationModel> getShopOrderOperationData(Sheet sheet) throws ParseException
    {
        List<ShopOrderOperationModel> shopOrderOperations = new ArrayList<>();
        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();

        for (Row row : sheet)
        {
            //skip the header row of the excel
            if (row.getRowNum() == 0)
            {
                continue;
            }
            DateFormat dateFormat = DateTimeUtil.getDefaultSimpleDateFormat();
            ShopOrderOperationModel shopOrderOperation = new ShopOrderOperationModel();
            int i = -1;

            shopOrderOperation.setOrderNo(dataFormatter.formatCellValue(row.getCell(++i)));
            shopOrderOperation.setOperationId(Integer.parseInt(dataFormatter.formatCellValue(row.getCell(++i))));
            shopOrderOperation.setOperationNo(Integer.parseInt(dataFormatter.formatCellValue(row.getCell(++i))));
            shopOrderOperation.setOperationDescription(dataFormatter.formatCellValue(row.getCell(++i)));
            shopOrderOperation.setOperationSequence(row.getCell(++i) == null ? null : Integer.parseInt(dataFormatter.formatCellValue(row.getCell(i))));
            shopOrderOperation.setWorkCenterRuntime(row.getCell(++i) == null ? null : Double.parseDouble(dataFormatter.formatCellValue(row.getCell(i))));
            shopOrderOperation.setLaborRunTime(row.getCell(++i) == null ? null : Double.parseDouble(dataFormatter.formatCellValue(row.getCell(i))));
            shopOrderOperation.setOpStartDate(row.getCell(++i) == null ? null : dateFormat.parse(dataFormatter.formatCellValue(row.getCell(i))));
            shopOrderOperation.setOpStartTime(row.getCell(++i) == null ? null : dateFormat.parse(dataFormatter.formatCellValue(row.getCell(i))));
            shopOrderOperation.setOpFinishDate(row.getCell(++i) == null ? null : dateFormat.parse(dataFormatter.formatCellValue(row.getCell(i))));
            shopOrderOperation.setOpFinishTime(row.getCell(++i) == null ? null : dateFormat.parse(dataFormatter.formatCellValue(row.getCell(i))));
            shopOrderOperation.setQuantity(Integer.parseInt(dataFormatter.formatCellValue(row.getCell(++i))));
            shopOrderOperation.setWorkCenterType(dataFormatter.formatCellValue(row.getCell(++i)));
            shopOrderOperation.setWorkCenterNo(dataFormatter.formatCellValue(row.getCell(++i)));
            shopOrderOperation.setOperationStatus(OperationStatus.valueOf(dataFormatter.formatCellValue(row.getCell(++i))));

            shopOrderOperations.add(shopOrderOperation);
            System.out.println();
        }
        return shopOrderOperations;
    }

    private List<WorkCenterModel> getWorkCenterData(Sheet sheet) throws ParseException
    {
        List<WorkCenterModel> workCenters = new ArrayList<>();

        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();

        for (Row row : sheet)
        {
            //skip the header row of the excel
            if (row.getRowNum() == 0)
            {
                continue;
            }
            WorkCenterModel workCenter = new WorkCenterModel();
            int i = -1;

            workCenter.setWorkCenterNo(dataFormatter.formatCellValue(row.getCell(++i)));
            workCenter.setWorkCenterType(dataFormatter.formatCellValue(row.getCell(++i)));
            workCenter.setWorkCenterDescription(dataFormatter.formatCellValue(row.getCell(++i)));
            workCenter.setWorkCenterCapacity(dataFormatter.formatCellValue(row.getCell(++i)));
            workCenters.add(workCenter);
        }

        return workCenters;
    }

    private List<WorkCenterOpAllocModel> getWorkCenterOpAllocations(Sheet sheet) throws ParseException
    {
        List<WorkCenterOpAllocModel> workCenterOpAllocations = new ArrayList<>();
        int noOfTimeBlocks = 8;

        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();

        for (Row row : sheet)
        {
            //skip the header row of the excel
            if (row.getRowNum() == 0)
            {
                continue;
            }
            DateFormat dateFormat = DateTimeUtil.getDefaultSimpleDateFormat();
            WorkCenterOpAllocModel workCenterOpAllocation = new WorkCenterOpAllocModel();
            int i = -1;

            workCenterOpAllocation.setWorkCenterNo(dataFormatter.formatCellValue(row.getCell(++i)));
            workCenterOpAllocation.setOperationDate(row.getCell(++i) == null ? null : dateFormat.parse(dataFormatter.formatCellValue(row.getCell(i))));
            workCenterOpAllocation.setTimeBlockAllocation(new HashMap<String, Integer>());
            int timeBlockId = 1;
            for (int j = ++i; j < i + noOfTimeBlocks; j++)
            {
                if (row.getCell(j) != null)
                {
                    workCenterOpAllocation.AddToTimeBlockAllocation("TB" + timeBlockId, Integer.parseInt(dataFormatter.formatCellValue(row.getCell(j))));
                } else
                {
                    workCenterOpAllocation.AddToTimeBlockAllocation("TB" + timeBlockId, 0);
                }
                timeBlockId++;
            }
            workCenterOpAllocations.add(workCenterOpAllocation);
        }

        return workCenterOpAllocations;
    }

}
