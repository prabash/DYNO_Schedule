/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.schedule.data;

import dyno.schedule.enums.DataGetMethod;
import dyno.schedule.models.WorkCenterModel;
import java.util.List;

/**
 *
 * @author Prabash
 */
public class WorkCenterDataManager
{
    private DataGetMethod getMethod;

    public WorkCenterDataManager(DataGetMethod getMethod)
    {
        this.getMethod = getMethod;
    }

    public List<WorkCenterModel> loadWorkCenterDetails()
    {
        switch (getMethod)
        {
            case Database:
            {
                return null;
            }
            case Excel:
            {
                return getWorkCenterDetailsFromExcel();
            }
            default:
            {
                return null;
            }
        }
    }
    
    private List<WorkCenterModel> getWorkCenterDetailsFromExcel()
    {
        ExcelManager excelMgr = new ExcelManager();
        List<WorkCenterModel> workCenters = excelMgr.getExcelData("WorkCenters");

        return workCenters;
    }
}
