package com.morefun.XSanGo.sign;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/活动和礼包/每日签到脚本.xls", sheetName = "签到累积礼包", beginRow = 1)
public class TotalSignGiftPacksT {
	@ExcelColumn(index = 0)
	public String name;
	@ExcelColumn(index = 1)
	public int timeLimit;
	@ExcelColumn(index = 2)
	public String gift;

	/**
	 * 是否显示特效
	 */
	@ExcelColumn(index = 3)
	public int showSpecialEffect;
}
