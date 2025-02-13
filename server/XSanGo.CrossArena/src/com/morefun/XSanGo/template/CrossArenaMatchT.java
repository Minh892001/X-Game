package com.morefun.XSanGo.template;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(beginRow = 2, fileName = "script/跨服竞技场.xls", sheetName = "竞技场匹配")
public class CrossArenaMatchT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public int beginRank;

	@ExcelColumn(index = 2)
	public int endRank;

	@ExcelColumn(index = 3)
	public int beginMatch1;

	@ExcelColumn(index = 4)
	public int endMatch1;

	@ExcelColumn(index = 5)
	public int beginMatch2;

	@ExcelColumn(index = 6)
	public int endMatch2;

	@ExcelColumn(index = 7)
	public int beginMatch3;

	@ExcelColumn(index = 8)
	public int endMatch3;
}
