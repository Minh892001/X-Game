package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/活动和礼包/蹴鞠配置表.xls", sheetName = "基础参数", beginRow = 2)
public class FootballConfT {
	@ExcelColumn(index = 0)
	public int isOpen;

	@ExcelColumn(index = 1)
	public int openLevel;
	
	@ExcelColumn(index = 2)
	public int openVipLevel;

	@ExcelColumn(index = 3)
	public String startDate;

	@ExcelColumn(index = 4)
	public String endDate;

	@ExcelColumn(index = 5)
	public String overDate;

	@ExcelColumn(index = 6)
	public int rankSize;

	@ExcelColumn(index = 7)
	public int closeDay;
	
	@ExcelColumn(index = 8)
	public int giveNum;
	
	@ExcelColumn(index = 9)
	public String itemId;
	
	@ExcelColumn(index = 10)
	public String shopDate;
}
