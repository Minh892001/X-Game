package com.morefun.XSanGo.faction;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(beginRow = 2, fileName = "script/副本和怪物/公会副本.xls", sheetName = "通关排名奖励")
public class CopyRankAwardT {
	@ExcelColumn(index = 0)
	public int rank;

	@ExcelColumn(index = 1)
	public int scale;// 万分比例

}
