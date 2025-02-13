package com.morefun.XSanGo.ArenaRank;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/互动和聊天/竞技场商城脚本.xls", beginRow = 2, sheetName = "商城刷新费用")
public class ArenaMallRefreshT {
	//次数
	@ExcelColumn(index = 0)
	public int Time;
	
	//花费元宝
	@ExcelColumn(index = 1)
	public int Cost;
	
}
