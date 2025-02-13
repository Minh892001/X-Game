package com.morefun.XSanGo.collect;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 名将召唤，开放时间和刷新时间配置
 * 
 * @author qinguofeng
 * */
@ExcelTable(fileName = "script/武将相关/魂魄转盘脚本.xls", beginRow = 2, sheetName = "刷新控制")
public class CollectTimeSettingT {
    @ExcelColumn(index = 0)
    public String name;
    @ExcelColumn(index = 1)
    public int type;
    @ExcelColumn(index = 2)
    public int canRefresh;
    @ExcelColumn(index = 3)
    public int goldConsume;
    @ExcelColumn(index = 4)
    public int autoRefresh;
    @ExcelColumn(index = 5)
    public String resetTime;
    @ExcelColumn(index = 6)
    public String timelimitStart;
    @ExcelColumn(index = 7)
    public String timelimitEnd;
}
