package com.morefun.XSanGo.superCharge;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/活动和礼包/超级充值.xls", beginRow = 2,sheetName = "基本参数")
public class SuperChargeBaseParamT {
	@ExcelColumn(index = 0)
	public String desc;
	
	@ExcelColumn(index = 1)
	public int raffleNum;
	
	@ExcelColumn(index = 2)
	public String item;
	
	@ExcelColumn(index = 3)
	public String helpInfo;
	
	@ExcelColumn(index = 4)
	public int showNum;
}
