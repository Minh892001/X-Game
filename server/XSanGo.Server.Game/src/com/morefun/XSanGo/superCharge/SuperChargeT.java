package com.morefun.XSanGo.superCharge;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/活动和礼包/超级充值.xls", beginRow = 2,sheetName = "今日累充")
public class SuperChargeT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public int money;

	@ExcelColumn(index = 2)
	public int rebate;

}
