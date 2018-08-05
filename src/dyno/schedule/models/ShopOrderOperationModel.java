/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.schedule.models;

import dyno.schedule.enums.OperationStatus;
import java.util.Date;

/**
 *
 * @author Prabash
 */
public class ShopOrderOperationModel
{
    private String orderNo;
    private int operationId;
    private int operationNo;
    private String workCenterNo;
    private String workCenterType;
    private String operationDescription;
    private int operationSequence;
    private double workCenterRuntime;
    private double laborRunTime;
    private Date opStartDate;
    private Date opStartTime;
    private Date opFinishDate;
    private Date opFinishTime;
    private int quantity;
    private OperationStatus operationStatus;

    /**
     * @return the orderNo
     */
    public String getOrderNo()
    {
        return orderNo;
    }

    /**
     * @param orderNo the orderNo to set
     */
    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    /**
     * @return the operationNo
     */
    public int getOperationNo()
    {
        return operationNo;
    }

    /**
     * @param operationNo the operationNo to set
     */
    public void setOperationNo(int operationNo)
    {
        this.operationNo = operationNo;
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
     * @return the operationDescription
     */
    public String getOperationDescription()
    {
        return operationDescription;
    }

    /**
     * @param operationDescription the operationDescription to set
     */
    public void setOperationDescription(String operationDescription)
    {
        this.operationDescription = operationDescription;
    }

    /**
     * @return the operationSequence
     */
    public int getOperationSequence()
    {
        return operationSequence;
    }

    /**
     * @param operationSequence the operationSequence to set
     */
    public void setOperationSequence(int operationSequence)
    {
        this.operationSequence = operationSequence;
    }

    /**
     * @return the workCenterRuntime
     */
    public double getWorkCenterRuntime()
    {
        return workCenterRuntime;
    }

    /**
     * @param workCenterRuntime the workCenterRuntime to set
     */
    public void setWorkCenterRuntime(double workCenterRuntime)
    {
        this.workCenterRuntime = workCenterRuntime;
    }

    /**
     * @return the laborRunTime
     */
    public double getLaborRunTime()
    {
        return laborRunTime;
    }

    /**
     * @param laborRunTime the laborRunTime to set
     */
    public void setLaborRunTime(double laborRunTime)
    {
        this.laborRunTime = laborRunTime;
    }

    /**
     * @return the opStartDate
     */
    public Date getOpStartDate()
    {
        return opStartDate;
    }

    /**
     * @param opStartDate the opStartDate to set
     */
    public void setOpStartDate(Date opStartDate)
    {
        this.opStartDate = opStartDate;
    }

    /**
     * @return the opStartTime
     */
    public Date getOpStartTime()
    {
        return opStartTime;
    }

    /**
     * @param opStartTime the opStartTime to set
     */
    public void setOpStartTime(Date opStartTime)
    {
        this.opStartTime = opStartTime;
    }

    /**
     * @return the opFinishDate
     */
    public Date getOpFinishDate()
    {
        return opFinishDate;
    }

    /**
     * @param opFinishDate the opFinishDate to set
     */
    public void setOpFinishDate(Date opFinishDate)
    {
        this.opFinishDate = opFinishDate;
    }

    /**
     * @return the opFinishTime
     */
    public Date getOpFinishTime()
    {
        return opFinishTime;
    }

    /**
     * @param opFinishTime the opFinishTime to set
     */
    public void setOpFinishTime(Date opFinishTime)
    {
        this.opFinishTime = opFinishTime;
    }

    /**
     * @return the quantity
     */
    public int getQuantity()
    {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    /**
     * @return the operationStatus
     */
    public OperationStatus getOperationStatus()
    {
        return operationStatus;
    }

    /**
     * @param operationStatus the operationStatus to set
     */
    public void setOperationStatus(OperationStatus operationStatus)
    {
        this.operationStatus = operationStatus;
    }

    /**
     * @return the operationId
     */
    public int getOperationId()
    {
        return operationId;
    }

    /**
     * @param operationId the operationId to set
     */
    public void setOperationId(int operationId)
    {
        this.operationId = operationId;
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
}
