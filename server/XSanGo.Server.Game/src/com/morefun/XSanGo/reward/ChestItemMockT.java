package com.morefun.XSanGo.reward;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/道具相关/掉落算法.xls", sheetName = "开宝箱伪随机算法")
public class ChestItemMockT {
	@ExcelColumn(index = 1)
	public String chestItem;

	@ExcelColumn(index = 2)
	public int minNum;

	@ExcelColumn(index = 3)
	public int maxNum;

	@ExcelColumn(index = 4)
	public String mockTc;

}
