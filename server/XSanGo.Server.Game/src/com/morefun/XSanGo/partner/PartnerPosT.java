package com.morefun.XSanGo.partner;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(beginRow = 2, fileName = "script/武将相关/伙伴脚本.xls", sheetName = "伙伴位")
public class PartnerPosT {
	
	@ExcelColumn(index = 0)
	public int id;
	@ExcelColumn(index = 1)
	public String position;
	@ExcelColumn(index = 2)
	public int level;
	@ExcelColumn(index = 3)
	public int type;
	@ExcelColumn(index = 4)
	public int amount;
	@ExcelColumn(index = 5)
	public int vipLevel;
	@ExcelColumn(index = 6)
	public int lockPay;
	@ExcelColumn(index = 7)
	public int openCondition;
	
}
