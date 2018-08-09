/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.schedule.models;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Prabash
 */
public class WorkCenterOpAllocModel
{
    private String workCenterNo;
    private Date operationDate;
    private HashMap<String, Integer> timeBlockAllocation;

    public WorkCenterOpAllocModel()
    {
        timeBlockAllocation = new HashMap<>();
    }
    
    /**
     * @return the workCenterNo
     */
    public String getWorkCenterNo()
    {
        return workCenterNo;
    }

    /**
     * @param workCenterNo the workCenterNo to set
     */
    public void setWorkCenterNo(String workCenterNo)
    {
        this.workCenterNo = workCenterNo;
    }

    /**
     * @return the operationDate
     */
    public Date getOperationDate()
    {
        return operationDate;
    }

    /**
     * @param operationDate the operationDate to set
     */
    public void setOperationDate(Date operationDate)
    {
        this.operationDate = operationDate;
    }

    /**
     * @return the timeBlockAllocation
     */
    public HashMap<String, Integer> getTimeBlockAllocation()
    {
        return timeBlockAllocation;
    }

    /**
     * @param timeBlockAllocation the timeBlockAllocation to set
     */
    public void setTimeBlockAllocation(HashMap<String, Integer> timeBlockAllocation)
    {
        this.timeBlockAllocation = timeBlockAllocation;
    }
    
    public void AddToTimeBlockAllocation(String timBlockName, int operationId)
    {
        this.timeBlockAllocation.put(timBlockName, operationId);
    }
}
