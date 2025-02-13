package com.morefun.XSanGo.ArenaRank;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/互动和聊天/竞技场商城脚本.xls", beginRow = 2, sheetName = "历史排名奖励")
public class ArenaLevelAwardT {
	//开始等级
	@ExcelColumn(index = 0)
	public int start;
	
	//结束等级
	@ExcelColumn(index = 1)
	public int end;
	
	//元宝比值
	@ExcelColumn(index = 2)
	public float rmby;
	
	//区间累积
	@ExcelColumn(index = 3)
	public int SumPart;
	
	//总累积
	@ExcelColumn(index = 4)
	public int sum;
}
