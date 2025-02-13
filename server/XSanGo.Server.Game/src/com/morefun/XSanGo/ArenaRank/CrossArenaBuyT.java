package com.morefun.XSanGo.ArenaRank;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/互动和聊天/跨服竞技场.xls", beginRow = 2, sheetName = "购买挑战次数")
public class CrossArenaBuyT {
	//次数
	@ExcelColumn(index = 0)
	public int count;
	
	//花费元宝
	@ExcelColumn(index = 1)
	public int cost;
	
	//获得挑战令数量
	@ExcelColumn(index = 2)
	public int addNum;
	
	//需要vip等级
	@ExcelColumn(index = 3)
	public int vipLv;
}
