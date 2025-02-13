package com.morefun.XSanGo.template;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/比武大会.xls", beginRow = 2, sheetName = "资格赛匹配")
public class CrossMatchT {
	@ExcelColumn(index = 0)
	public int upRank1;

	@ExcelColumn(index = 1)
	public int nextRank1;

	@ExcelColumn(index = 2)
	public int upRank2;

	@ExcelColumn(index = 3)
	public int nextRank2;

	@ExcelColumn(index = 4)
	public int upRank3;

	@ExcelColumn(index = 5)
	public int nextRank3;
}
