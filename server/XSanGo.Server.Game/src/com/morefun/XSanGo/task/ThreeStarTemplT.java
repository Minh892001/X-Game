package com.morefun.XSanGo.task;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/任务脚本/任务脚本.xls", beginRow = 2)
public class ThreeStarTemplT {
	@ExcelColumn(index = 0)
	public int day;

	@ExcelColumn(index = 1)
	public String item1;
	
	@ExcelColumn(index = 2)
	public int itemNum1;
	
	@ExcelColumn(index = 3)
	public String item2;
	
	@ExcelColumn(index = 4)
	public int itemNum2;
	
	@ExcelColumn(index = 5)
	public String item3;
	
	@ExcelColumn(index = 6)
	public int itemNum3;
	
	@ExcelColumn(index = 7)
	public String icon;
	
	@ExcelColumn(index = 8)
	public String title;
}
