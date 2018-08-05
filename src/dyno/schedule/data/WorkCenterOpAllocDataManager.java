/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.schedule.data;

import dyno.schedule.utils.DateTimeUtil;
import dyno.schedule.enums.DataGetMethod;
import dyno.schedule.models.WorkCenterOpAllocModel;
import dyno.schedule.utils.CollectionUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author Prabash
 */
public class WorkCenterOpAllocDataManager
{

    private final DataGetMethod getMethod;
    private static List<WorkCenterOpAllocModel> workCenterOpAllocations;

    public WorkCenterOpAllocDataManager(DataGetMethod getMethod)
    {
        this.getMethod = getMethod;
    }

    public List<WorkCenterOpAllocModel> loadWorkCenterOpAllocDetails()
    {
        switch (getMethod)
        {
            case Database:
            {
                return null;
            }
            case Excel:
            {
                workCenterOpAllocations = getWorkCenterOpAllocDetailsFromExcel();
            }
        }
        return workCenterOpAllocations;
    }

    private List<WorkCenterOpAllocModel> getWorkCenterOpAllocDetailsFromExcel()
    {
        ExcelManager excelMgr = new ExcelManager();
        List<WorkCenterOpAllocModel> workCenterOpAlloc = excelMgr.getExcelData("WorkCenterOpAllocations");

        return workCenterOpAlloc;
    }

    /**
     * *
     * this method will return the best date time offer for the work center no
     * and the required date TODO: should also incorporate scheduling direction
     *
     * @param workCenterNo
     * @param requiredDate
     * @return
     */
    public static Date getBestDateTimeOffer(String workCenterNo, Date requiredDate)
    {
        Date bestDateTimeOffer = null;
        // get the allocation for the requiredDate. 
        // TODO: if the requiredDate allocation is full next possible date should be taken
        List<WorkCenterOpAllocModel> specifiedWorkCenterALloc = workCenterOpAllocations.stream()
                .filter(rec -> rec.getWorkCenterNo().equals(workCenterNo) && (rec.getOperationDate().compareTo(requiredDate) == 0))
                .collect(Collectors.toList());

        if (specifiedWorkCenterALloc != null)
        {
            WorkCenterOpAllocModel bestDateAllocation = specifiedWorkCenterALloc.get(0);
            bestDateTimeOffer = getBestTimeOffer(bestDateAllocation);
        }
        return bestDateTimeOffer;
    }

    /**
     * *
     * this method will get the date allocation and return the earliest
     * available timeblock value
     *
     * @param allocation
     * @return
     */
    public static Date getBestTimeOffer(WorkCenterOpAllocModel allocation)
    {
        Date bestTimeOffer = null;

        // get the hashmap with timeblock assignment details
        HashMap<String, Integer> timeBlockDetails = allocation.getTimeBlockAllocation();

        //sort the hashmap by its keys
        SortedSet<String> keys = new TreeSet<>(timeBlockDetails.keySet());
        for (String key : keys)
        {
            if (timeBlockDetails.get(key) == 0)
            {
                String startingDate = DateTimeUtil.getDefaultSimpleDateFormat().format(allocation.getOperationDate());
                String startingTime = DateTimeUtil.getDefaultSimpleTimeFormat().format(getTimeBlockValue(key));
                LocalDate datePart = LocalDate.parse(startingDate, DateTimeUtil.getDefaultDateFormat());
                LocalTime timePart = LocalTime.parse(startingTime);
                bestTimeOffer = DateTimeUtil.asDate(LocalDateTime.of(datePart, timePart));

                break;
            }
        }
        return bestTimeOffer;
    }

    private static Date getTimeBlockValue(String timeBlock)
    {
        Date timeBlockValue = null;
        try
        {
            SimpleDateFormat dateFormat = DateTimeUtil.getDefaultSimpleTimeFormat();
            switch (timeBlock)
            {
                case "TB1":
                    timeBlockValue = dateFormat.parse("08:00:00"); break;
                case "TB2":
                    timeBlockValue = dateFormat.parse("09:00:00"); break;
                case "TB3":
                    timeBlockValue = dateFormat.parse("10:00:00"); break;
                case "TB4":
                    timeBlockValue = dateFormat.parse("11:00:00"); break;
                case "TB5":
                    timeBlockValue = dateFormat.parse("13:00:00"); break;
                case "TB6":
                    timeBlockValue = dateFormat.parse("14:00:00"); break;
                case "TB7":
                    timeBlockValue = dateFormat.parse("15:00:00"); break;
                case "TB8":
                    timeBlockValue = dateFormat.parse("16:00:00"); break;

            }
            return timeBlockValue;
        } catch (ParseException ex)
        {
            Logger.getLogger(WorkCenterOpAllocDataManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return timeBlockValue;
    }

}
