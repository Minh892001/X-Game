package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/活动和礼包/蹴鞠配置表.xls", sheetName = "商品列表", beginRow = 2)
public class FootballShopT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public String itemId;
	
	@ExcelColumn(index = 2)
	public int price;
}
