/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.schedule.data;

import dyno.schedule.utils.DateTimeUtil;
import dyno.schedule.enums.DataGetMethod;
import dyno.schedule.models.WorkCenterOpAllocModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;

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
     * and the required date TODO: should also incorporate scheduling direction and time factors
     *
     * @param workCenterNo
     * @param requiredDate
     * @return
     */
    public static Date getBestDateTimeOffer(String workCenterNo, Date requiredDate)
    {
        Date bestDateTimeOffer = null;
        // get the allocation for the work center. 
        // TODO: if the requiredDate allocation is full next possible date should be taken
        List<WorkCenterOpAllocModel> workCenterALloc = workCenterOpAllocations.stream()
                .filter(rec -> rec.getWorkCenterNo().equals(workCenterNo))
                .collect(Collectors.toList());

        if (workCenterALloc != null)
        {
            // sort the work center allocations by date on the ascending order
            Collections.sort(workCenterALloc, new Comparator<WorkCenterOpAllocModel>()
            {
                public int compare(WorkCenterOpAllocModel o1, WorkCenterOpAllocModel o2)
                {
                    return o1.getOperationDate().compareTo(o2.getOperationDate());
                }
            });
            
            WorkCenterOpAllocModel bestDateAllocation = workCenterALloc.get(0);
            bestDateTimeOffer = getBestDateOffer(workCenterALloc, requiredDate);
            //bestDateTimeOffer = getBestTimeOffer(bestDateAllocation);
        }
        return bestDateTimeOffer;
    }

    private static Date getBestDateOffer(List<WorkCenterOpAllocModel> workCenterAlloc, Date requiredDate)
    {
        Date bestDate = null;
        // convert the required date to joda datetime
        DateTime requiredDateTime = new DateTime(requiredDate);
        
        for (WorkCenterOpAllocModel workCenterOpAlloc : workCenterAlloc)
        {
            // convert the work center date to joda datetime
            DateTime workCenterOpDate = new DateTime(workCenterOpAlloc.getOperationDate());
            if (workCenterOpDate.toLocalDate().equals(requiredDateTime.toLocalDate()) || workCenterOpDate.toLocalDate().isAfter(requiredDateTime.toLocalDate()))
            {
                bestDate = getBestTimeOffer(workCenterOpAlloc, requiredDateTime.toLocalTime());
                break;
            }
        }
        
        return bestDate;
    }
    
    /**
     * *
     * this method will get the date allocation and return the earliest
     * available timeblock value
     *
     * @param allocation
     * @return
     */
    public static Date getBestTimeOffer(WorkCenterOpAllocModel allocation, LocalTime reqTime)
    {
        Date bestTimeOffer = null;

        // get the hashmap with timeblock assignment details
        HashMap<String, Integer> timeBlockDetails = allocation.getTimeBlockAllocation();

        //sort the hashmap by its keys
        SortedSet<String> keys = new TreeSet<>(timeBlockDetails.keySet());
        for (String key : keys)
        {
            LocalTime timeBlockStartTime = new DateTime(getTimeBlockValue(key)).toLocalTime();
            if ((timeBlockStartTime.equals(reqTime) || timeBlockStartTime.isAfter(reqTime)) && timeBlockDetails.get(key) == 0)
            {
                bestTimeOffer = DateTimeUtil.ConcatenateDateTime(allocation.getOperationDate(), getTimeBlockValue(key));
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
            SimpleDateFormat timeFormat = DateTimeUtil.getDefaultSimpleTimeFormat();
            switch (timeBlock)
            {
                case "TB1":
                    timeBlockValue = timeFormat.parse("08:00:00");
                    break;
                case "TB2":
                    timeBlockValue = timeFormat.parse("09:00:00");
                    break;
                case "TB3":
                    timeBlockValue = timeFormat.parse("10:00:00");
                    break;
                case "TB4":
                    timeBlockValue = timeFormat.parse("11:00:00");
                    break;
                case "TB5":
                    timeBlockValue = timeFormat.parse("13:00:00");
                    break;
                case "TB6":
                    timeBlockValue = timeFormat.parse("14:00:00");
                    break;
                case "TB7":
                    timeBlockValue = timeFormat.parse("15:00:00");
                    break;
                case "TB8":
                    timeBlockValue = timeFormat.parse("16:00:00");
                    break;
            }
            return timeBlockValue;
        } catch (ParseException ex)
        {
            Logger.getLogger(WorkCenterOpAllocDataManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return timeBlockValue;
    }

}
