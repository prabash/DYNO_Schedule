/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.schedule.models;

import java.util.Date;
import dyno.schedule.enums.*;
import java.util.List;

/**
 *
 * @author Prabash
 */
public class ShopOrderModel
{

    private String OrderNo;
    private String Description;
    private Date CreatedDate;
    private String PartNo;
    private String StructureRevision;
    private String RoutingRevision;
    private Date RequiredDate;
    private Date StartDate;
    private Date FinishDate;
    private ShopOrderSchedulingDirection SchedulingDirection;
    private String CustomerNo;
    private ShopOrderScheduleStatus SchedulingStatus;
    private ShopOrderStatus ShopOrderStatus;
    private ShopOrderPriority Priority;
    private List<ShopOrderOperationModel> operations;

    /**
     * @return the OrderNo
     */
    public String getOrderNo()
    {
        return OrderNo;
    }

    /**
     * @param OrderNo the OrderNo to set
     */
    public void setOrderNo(String OrderNo)
    {
        this.OrderNo = OrderNo;
    }

    /**
     * @return the Description
     */
    public String getDescription()
    {
        return Description;
    }

    /**
     * @param Description the Description to set
     */
    public void setDescription(String Description)
    {
        this.Description = Description;
    }

    /**
     * @return the CreatedDate
     */
    public Date getCreatedDate()
    {
        return CreatedDate;
    }

    /**
     * @param CreatedDate the CreatedDate to set
     */
    public void setCreatedDate(Date CreatedDate)
    {
        this.CreatedDate = CreatedDate;
    }

    /**
     * @return the StructureRevision
     */
    public String getStructureRevision()
    {
        return StructureRevision;
    }

    /**
     * @param StructureRevision the StructureRevision to set
     */
    public void setStructureRevision(String StructureRevision)
    {
        this.StructureRevision = StructureRevision;
    }

    /**
     * @return the RoutingRevision
     */
    public String getRoutingRevision()
    {
        return RoutingRevision;
    }

    /**
     * @param RoutingRevision the RoutingRevision to set
     */
    public void setRoutingRevision(String RoutingRevision)
    {
        this.RoutingRevision = RoutingRevision;
    }

    /**
     * @return the RequiredDate
     */
    public Date getRequiredDate()
    {
        return RequiredDate;
    }

    /**
     * @param RequiredDate the RequiredDate to set
     */
    public void setRequiredDate(Date RequiredDate)
    {
        this.RequiredDate = RequiredDate;
    }

    /**
     * @return the StartDate
     */
    public Date getStartDate()
    {
        return StartDate;
    }

    /**
     * @param StartDate the StartDate to set
     */
    public void setStartDate(Date StartDate)
    {
        this.StartDate = StartDate;
    }

    /**
     * @return the FinishDate
     */
    public Date getFinishDate()
    {
        return FinishDate;
    }

    /**
     * @param FinishDate the FinishDate to set
     */
    public void setFinishDate(Date FinishDate)
    {
        this.FinishDate = FinishDate;
    }

    /**
     * @return the SchedulingDirection
     */
    public ShopOrderSchedulingDirection getSchedulingDirection()
    {
        return SchedulingDirection;
    }

    /**
     * @param SchedulingDirection the SchedulingDirection to set
     */
    public void setSchedulingDirection(ShopOrderSchedulingDirection SchedulingDirection)
    {
        this.SchedulingDirection = SchedulingDirection;
    }

    /**
     * @return the CustomerNo
     */
    public String getCustomerNo()
    {
        return CustomerNo;
    }

    /**
     * @param CustomerNo the CustomerNo to set
     */
    public void setCustomerNo(String CustomerNo)
    {
        this.CustomerNo = CustomerNo;
    }

    /**
     * @return the SchedulingStatus
     */
    public ShopOrderScheduleStatus getSchedulingStatus()
    {
        return SchedulingStatus;
    }

    /**
     * @param SchedulingStatus the SchedulingStatus to set
     */
    public void setSchedulingStatus(ShopOrderScheduleStatus SchedulingStatus)
    {
        this.SchedulingStatus = SchedulingStatus;
    }

    /**
     * @return the ShopOrderStatus
     */
    public ShopOrderStatus getShopOrderStatus()
    {
        return ShopOrderStatus;
    }

    /**
     * @param ShopOrderStatus the ShopOrderStatus to set
     */
    public void setShopOrderStatus(ShopOrderStatus ShopOrderStatus)
    {
        this.ShopOrderStatus = ShopOrderStatus;
    }

    /**
     * @return the Priority
     */
    public ShopOrderPriority getPriority()
    {
        return Priority;
    }

    /**
     * @param Priority the Priority to set
     */
    public void setPriority(ShopOrderPriority Priority)
    {
        this.Priority = Priority;
    }

    /**
     * @return the operations
     */
    public List<ShopOrderOperationModel> getOperations()
    {
        return operations;
    }

    /**
     * @param operations the operations to set
     */
    public void setOperations(List<ShopOrderOperationModel> operations)
    {
        this.operations = operations;
    }
    
    public boolean addOperation(ShopOrderOperationModel operation) throws Exception
    {
        try
        {
            if (operation != null)
            {
                getOperations().add(operation);
                return true;
            }
            else
                throw new Exception("operation details are empty!");
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    /**
     * @return the PartNo
     */
    public String getPartNo()
    {
        return PartNo;
    }

    /**
     * @param PartNo the PartNo to set
     */
    public void setPartNo(String PartNo)
    {
        this.PartNo = PartNo;
    }

}
