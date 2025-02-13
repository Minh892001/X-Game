package com.morefun.XSanGo.template;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 跨服战，积分奖励
 * 
 * @author lxm
 */
@ExcelTable(fileName = "script/比武大会.xls", beginRow = 2, sheetName = "累计积分奖励")
public class CrossScoreRewardT {
	/** 积分 */
	@ExcelColumn(index = 0)
	public int score;
	
	/** 奖励物品, 逗号分割每项，冒号分割奖励的id和数量 */
	@ExcelColumn(index = 1)
	public String items;
}
