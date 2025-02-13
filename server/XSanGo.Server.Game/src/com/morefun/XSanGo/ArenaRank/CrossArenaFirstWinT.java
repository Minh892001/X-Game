package com.morefun.XSanGo.ArenaRank;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(beginRow = 2, fileName = "script/互动和聊天/跨服竞技场.xls", sheetName = "每日首胜奖励")
public class CrossArenaFirstWinT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public int beginRank;

	@ExcelColumn(index = 2)
	public int endRank;

	@ExcelColumn(index = 3)
	public String item;
}
