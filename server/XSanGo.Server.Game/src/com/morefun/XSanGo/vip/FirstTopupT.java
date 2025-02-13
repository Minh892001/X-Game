package com.morefun.XSanGo.vip;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/参数配置脚本/VIP脚本.xls", sheetName = "每月首充", beginRow = 2)
public class FirstTopupT {

	@ExcelColumn(index = 0)
	public String RefreshTime;
	@ExcelColumn(index = 1)
	public String Item1ID;
	@ExcelColumn(index = 2)
	public String Name1;
	@ExcelColumn(index = 3)
	public String Num1;
	@ExcelColumn(index = 4)
	public String Item2ID;
	@ExcelColumn(index = 5)
	public String Name2;
	@ExcelColumn(index = 6)
	public String Num2;
	@ExcelColumn(index = 7)
	public String Item3ID;
	@ExcelColumn(index = 8)
	public String Name3;
	@ExcelColumn(index = 9)
	public String Num3;
	@ExcelColumn(index = 10)
	public String Item4ID;
	@ExcelColumn(index = 11)
	public String Name4;
	@ExcelColumn(index = 12)
	public String Num4;
}
