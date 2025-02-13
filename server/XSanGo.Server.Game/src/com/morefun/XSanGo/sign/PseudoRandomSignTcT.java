package com.morefun.XSanGo.sign;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/活动和礼包/每日签到脚本.xls", sheetName = "抽奖伪随机", beginRow = 1)
public class PseudoRandomSignTcT {
	@ExcelColumn(index = 0)
	public int min;
	@ExcelColumn(index = 1)
	public int max;
}
