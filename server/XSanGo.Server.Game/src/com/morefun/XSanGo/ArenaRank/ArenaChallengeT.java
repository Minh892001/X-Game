package com.morefun.XSanGo.ArenaRank;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/互动和聊天/竞技场商城脚本.xls", beginRow = 2, sheetName = "购买挑战令")
public class ArenaChallengeT {
	//次数
	@ExcelColumn(index = 0)
	public int Time;
	
	//花费元宝
	@ExcelColumn(index = 1)
	public int Cost;
	
	//获得挑战令数量
	@ExcelColumn(index = 2)
	public int Num;
	
	//需要vip等级
	@ExcelColumn(index = 3)
	public int VipLv;
}
