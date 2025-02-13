package com.morefun.XSanGo.task;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/任务脚本/任务脚本.xls", beginRow = 2)
public class TaskActivePointTemplT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public int point;

	@ExcelColumn(index = 2)
	public String award;
	
	@ExcelColumn(index = 3)
	public String iCon;
}
