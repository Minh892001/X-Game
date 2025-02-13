package com.morefun.XSanGo.itemChip;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/互动和聊天/掠夺脚本.xls", beginRow = 2, sheetName = "掠夺成功率")
public class ItemChipProbT {
	// 等级差
	@ExcelColumn(index = 0)
	public int clv;

	// 成功率
	@ExcelColumn(index = 1)
	public int probability;
}
