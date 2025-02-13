package com.morefun.XSanGo.crossServer;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 比武大会，购买挑战次数花费
 * 
 * @author guofeng.qin
 */
@ExcelTable(fileName = "script/互动和聊天/比武大会.xls", beginRow = 2, sheetName = "购买挑战次数")
public class TournamentFightCostT {
	/** 刷新次数 */
	@ExcelColumn(index = 0)
	public int count;
	/** 花费元宝 */
	@ExcelColumn(index = 1)
	public int cost;
	/** 获得数量 */
	@ExcelColumn(index = 2)
	public int getCount;
}
