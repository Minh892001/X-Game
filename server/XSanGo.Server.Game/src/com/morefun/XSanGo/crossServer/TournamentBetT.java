package com.morefun.XSanGo.crossServer;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 比武大会，押注
 * 
 * @author guofeng.qin
 */
@ExcelTable(fileName = "script/互动和聊天/比武大会.xls", beginRow = 2, sheetName = "竞赛花费")
public class TournamentBetT {
	/** 金额 */
	@ExcelColumn(index = 1)
	public String winTC;
	/** 奖励 */
	@ExcelColumn(index = 3)
	public String loseTC;
}
