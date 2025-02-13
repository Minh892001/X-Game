package com.morefun.XSanGo.ArenaRank;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(beginRow = 2, fileName = "script/互动和聊天/跨服竞技场.xls", sheetName = "跨服竞技场基本参数")
public class CrossArenaConfT {
	@ExcelColumn(index = 0)
	public int isOpen;

	@ExcelColumn(index = 1)
	public int openLevel;

	@ExcelColumn(index = 2)
	public int challengeNum;

	@ExcelColumn(index = 3)
	public String resetTime;

	@ExcelColumn(index = 4)
	public int clearCdYuanbao;

	@ExcelColumn(index = 5)
	public int rankSize;

	@ExcelColumn(index = 6)
	public int reportSize;
	
	@ExcelColumn(index = 9)
	public int cdSecond;
}
