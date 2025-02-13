package com.morefun.XSanGo.task;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/任务脚本/任务脚本.xls", beginRow = 2)
public class SevenTaskTemplT {
	@ExcelColumn(index = 0)
	public int day;

	@ExcelColumn(index = 1)
	public int id;
	
	@ExcelColumn(index = 2)
	public int type;
	
	@ExcelColumn(index = 3)
	public String content;
	
	@ExcelColumn(index = 4)
	public String item;
	
	@ExcelColumn(index = 5)
	public int itemNum;
	
	@ExcelColumn(index = 6)
	public String target;
	
	@ExcelColumn(index = 7)
	public String demand;
	
	@ExcelColumn(index = 8)
	public int maxNum;
	
	@ExcelColumn(index = 9)
	public String prompt;
}
