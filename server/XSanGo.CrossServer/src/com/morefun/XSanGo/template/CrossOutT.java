package com.morefun.XSanGo.template;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/比武大会.xls", beginRow = 2, sheetName = "淘汰赛赛程")
public class CrossOutT {

	@ExcelColumn(index = 0)
	public int people;

	@ExcelColumn(index = 1)
	public int winNum;

}
