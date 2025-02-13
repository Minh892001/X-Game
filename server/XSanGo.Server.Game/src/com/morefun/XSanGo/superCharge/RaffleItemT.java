package com.morefun.XSanGo.superCharge;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/活动和礼包/超级充值.xls", beginRow = 2,sheetName = "抽奖道具")
public class RaffleItemT {

	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public String itemId;

	@ExcelColumn(index = 2)
	public int num;
	
	@ExcelColumn(index = 3)
	public int weight;
	
	@ExcelColumn(index = 4)
	public int announceFlag;
	
	@ExcelColumn(index = 5)
	public int showFlag;
}
