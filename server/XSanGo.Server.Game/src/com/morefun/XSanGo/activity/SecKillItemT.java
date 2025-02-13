package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 秒杀物品
 * 
 * @author lixiongming
 *
 */
@ExcelTable(fileName = "script/活动和礼包/天天秒杀.xls", sheetName = "商品列表", beginRow = 2)
public class SecKillItemT {
	@ExcelColumn(index = 0)
	public int type;

	@ExcelColumn(index = 1)
	public int id;

	@ExcelColumn(index = 2)
	public String itemId;

	@ExcelColumn(index = 3)
	public int price;
	
	@ExcelColumn(index = 4)
	public int maxNum;//最大可购买数量
}
