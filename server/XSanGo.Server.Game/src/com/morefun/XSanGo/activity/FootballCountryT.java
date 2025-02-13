package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/活动和礼包/蹴鞠配置表.xls", sheetName = "国家编号", beginRow = 2)
public class FootballCountryT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public String name;
	
	@ExcelColumn(index = 2)
	public String flag;
	
	@ExcelColumn(index = 3)
	public String groupName;
}
