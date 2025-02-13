package com.morefun.XSanGo.ArenaRank;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/互动和聊天/竞技场商城脚本.xls", beginRow = 2, sheetName = "每日排名奖励")
public class ArenaRankAwareT {
	//次数
	@ExcelColumn(index = 0)
	public int id;
	
	//开始等级
	@ExcelColumn(index = 1)
	public int start;
	
	//结束等级
	@ExcelColumn(index = 2)
	public int end;
	
	//奖励的物品
	@ExcelColumn(index = 3)
	public String items;
	
}
