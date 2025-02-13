package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 聚宝盆物品
 * 
 * @author lixiongming
 *
 */
@ExcelTable(fileName = "script/活动和礼包/聚宝盆.xls", sheetName = "购买项", beginRow = 2)
public class CornucopiaItemT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public String itemId;

	@ExcelColumn(index = 2)
	public String itemName;

	@ExcelColumn(index = 3)
	public int price;

	@ExcelColumn(index = 4)
	public int num;

	@ExcelColumn(index = 5)
	public String tips;
}
