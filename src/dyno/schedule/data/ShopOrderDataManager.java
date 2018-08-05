/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.schedule.data;

import dyno.schedule.enums.DataGetMethod;
import dyno.schedule.models.ShopOrderModel;
import dyno.schedule.models.ShopOrderOperationModel;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Prabash
 */
public class ShopOrderDataManager
{
    private final DataGetMethod getMethod;
    private static List<ShopOrderModel> shopOrderDetails;

    public ShopOrderDataManager(DataGetMethod getMethod)
    {
        this.getMethod = getMethod;
    }

    public List<ShopOrderModel> loadShopOrderDetails()
    {
        switch (getMethod)
        {
            case Database:
            {
                return null;
            }
            case Excel:
            {
                shopOrderDetails = getShopOrderDetailsFromExcel();
            }
        }
        return shopOrderDetails;
    }

    private List<ShopOrderModel> getShopOrderDetailsFromExcel()
    {
        ExcelManager excelMgr = new ExcelManager();
        List<ShopOrderModel> shopOrders = excelMgr.getExcelData("ShopOrders");
        List<ShopOrderOperationModel> shopOrderOperations = excelMgr.getExcelData("ShopOrderOperations");

        for (ShopOrderModel shopOrder : shopOrders)
        {
            List<ShopOrderOperationModel> relatedOperations = shopOrderOperations.stream()
                    .filter(op -> op.getOrderNo().equals(shopOrder.getOrderNo()))
                    .collect(Collectors.toList());
            shopOrder.setOperations(relatedOperations);
        }

        return shopOrders;
    }
}
