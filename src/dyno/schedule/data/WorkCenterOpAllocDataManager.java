/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.schedule.data;

import dyno.schedule.enums.DataGetMethod;
import dyno.schedule.models.WorkCenterOpAllocModel;
import java.util.List;

/**
 *
 * @author Prabash
 */
public class WorkCenterOpAllocDataManager
{
    private DataGetMethod getMethod;

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
                return getWorkCenterOpAllocDetailsFromExcel();
            }
            default:
            {
                return null;
            }
        }
    }
    
        private List<WorkCenterOpAllocModel> getWorkCenterOpAllocDetailsFromExcel()
    {
        ExcelManager excelMgr = new ExcelManager();
        List<WorkCenterOpAllocModel> workCenterOpAlloc = excelMgr.getExcelData("WorkCenterOpAllocations");

        return workCenterOpAlloc;
    }
}
