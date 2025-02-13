package com.morefun.XSanGo.collect;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * @author guofeng.qin
 */
@ExcelTable(fileName = "script/武将相关/魂魄转盘脚本.xls", beginRow = 1, sheetName = "元宝召唤费用")
public class CollectHeroSoulGoldPriceT {
	@ExcelColumn(index = 0)
	public int num;
	@ExcelColumn(index = 1)
	public int price;
}
