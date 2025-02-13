package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/活动和礼包/蹴鞠配置表.xls", sheetName = "购买花费", beginRow = 2)
public class FootballBuyT {
	@ExcelColumn(index = 0)
	public int num;

	@ExcelColumn(index = 1)
	public int yuanbao;
	
	@ExcelColumn(index = 2)
	public int trophyNum;
}
