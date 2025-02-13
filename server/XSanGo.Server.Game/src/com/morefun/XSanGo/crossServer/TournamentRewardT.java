package com.morefun.XSanGo.crossServer;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 跨服战，每个阶段奖励
 * 
 * @author guofeng.qin
 */
@ExcelTable(fileName = "script/互动和聊天/比武大会.xls", beginRow = 2, sheetName = "淘汰赛奖励")
public class TournamentRewardT {
	/** 类型 */
	@ExcelColumn(index = 0)
	public int type;
	/** 奖励物品, 逗号分割每项，冒号分割奖励的id和数量 */
	@ExcelColumn(index = 1)
	public String items;
}
