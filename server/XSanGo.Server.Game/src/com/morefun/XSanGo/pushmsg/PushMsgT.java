package com.morefun.XSanGo.pushmsg;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 推送消息配置模版
 * 
 * @author qinguofeng
 * */
@ExcelTable(fileName = "script/推送消息/推送消息.xls", beginRow = 1, sheetName = "推送消息")
public class PushMsgT {
    @ExcelColumn(index = 0)
    public int id;
    @ExcelColumn(index = 1)
    public int status;
    @ExcelColumn(index = 2)
    public int type; // 类型： 即时 0、延时 1
    @ExcelColumn(index = 3)
    public String time; // 延迟时间，延迟多少秒之后执行
    @ExcelColumn(index = 4)
    public String message;
    @ExcelColumn(index = 5)
    public String title;
    @ExcelColumn(index = 6)
    public String info;
    @ExcelColumn(index = 7)
    public int interval;
}
