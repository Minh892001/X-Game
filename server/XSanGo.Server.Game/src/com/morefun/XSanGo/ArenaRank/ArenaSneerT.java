package com.morefun.XSanGo.ArenaRank;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/互动和聊天/竞技场商城脚本.xls", beginRow = 2, sheetName = "嘲讽模式")
public class ArenaSneerT {
	//次数
	@ExcelColumn(index = 0)
	public int id;
	
	//花费元宝
	@ExcelColumn(index = 1)
	public int cost;
	
	//获得奖励
	@ExcelColumn(index = 2)
	public int gain;
}
