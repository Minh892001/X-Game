package com.morefun.XSanGo.ArenaRank;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/互动和聊天/竞技场商城脚本.xls", beginRow = 2, sheetName = "CD重置花费")
public class ArenaClearCDT {
	//次数
	@ExcelColumn(index = 0)
	public int time;
	
	//花费
	@ExcelColumn(index = 1)
	public int cost;
}
