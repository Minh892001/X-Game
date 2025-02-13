package com.morefun.XSanGo;

import java.util.Date;

import com.morefun.XSanGo.script.DataFormat;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "程序配置.xls", sheetName = "定时指令")
public class TimerTaskT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public String name;

	@ExcelColumn(index = 2, dataType = DataFormat.DateTime)
	public Date firstExeTime;

	@ExcelColumn(index = 3)
	public int repeat;

	@ExcelColumn(index = 4)
	public int intervalHours;

	@ExcelColumn(index = 5)
	public String script;
}
