package com.morefun.XSanGo.crossServer;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 比武大会，跨服区间
 * 
 * @author guofeng.qin
 */
@ExcelTable(fileName = "script/互动和聊天/比武大会.xls", beginRow = 2, sheetName = "跨服区间")
public class TournamentAreaConfigT {
	/** 跨服编号 */
	@ExcelColumn(index = 0)
	public int id;
	/** 区服列表 */
	@ExcelColumn(index = 1)
	public String server;
	/** 报名等级 */
	@ExcelColumn(index = 2)
	public int lvLimit;
}
