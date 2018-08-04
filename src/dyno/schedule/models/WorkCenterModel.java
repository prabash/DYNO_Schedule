/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.schedule.models;

/**
 *
 * @author Prabash
 */
public class WorkCenterModel
{
    private String workCenterNo;
    private String workCenterType;
    private String workCenterDescription;
    private String workCenterCapacity;

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
     * @return the workCenterType
     */
    public String getWorkCenterType()
    {
        return workCenterType;
    }

    /**
     * @param workCenterType the workCenterType to set
     */
    public void setWorkCenterType(String workCenterType)
    {
        this.workCenterType = workCenterType;
    }

    /**
     * @return the workCenterDescription
     */
    public String getWorkCenterDescription()
    {
        return workCenterDescription;
    }

    /**
     * @param workCenterDescription the workCenterDescription to set
     */
    public void setWorkCenterDescription(String workCenterDescription)
    {
        this.workCenterDescription = workCenterDescription;
    }

    /**
     * @return the workCenterCapacity
     */
    public String getWorkCenterCapacity()
    {
        return workCenterCapacity;
    }

    /**
     * @param workCenterCapacity the workCenterCapacity to set
     */
    public void setWorkCenterCapacity(String workCenterCapacity)
    {
        this.workCenterCapacity = workCenterCapacity;
    }
}
