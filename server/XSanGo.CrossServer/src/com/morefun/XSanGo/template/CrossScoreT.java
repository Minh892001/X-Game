package com.morefun.XSanGo.template;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/比武大会.xls", beginRow = 2, sheetName = "资格赛积分算法")
public class CrossScoreT {
	
	@ExcelColumn(index = 0)
	public int scale;

	@ExcelColumn(index = 1)
	public int addScore;
}
