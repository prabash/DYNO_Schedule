/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.schedule.data;

import dyno.schedule.enums.DataGetMethod;

/**
 *
 * @author Prabash
 */
public class DataManager
{
    private static final DataGetMethod dataMethod = DataGetMethod.Excel;
    
    public static DataGetMethod getDataMethod()
    {
        return dataMethod;
    }
}
