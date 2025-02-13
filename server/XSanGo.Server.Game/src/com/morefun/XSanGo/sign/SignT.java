package com.morefun.XSanGo.sign;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/活动和礼包/每日签到脚本.xls", sheetName = "每日签到礼包", beginRow = 1)
public class SignT {
	@ExcelColumn(index = 0)
	public int month;
	@ExcelColumn(index = 1)
	public int day;
	@ExcelColumn(index = 2)
	public String tc;
	@ExcelColumn(index = 3)
	public int vipLimit;
	@ExcelColumn(index = 4)
	public int quality;
	@ExcelColumn(index = 5)
	public int resignCost;
}
