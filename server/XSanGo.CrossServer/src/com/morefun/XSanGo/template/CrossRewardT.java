package com.morefun.XSanGo.template;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 跨服战，每个阶段奖励
 * 
 * @author lxm
 */
@ExcelTable(fileName = "script/比武大会.xls", beginRow = 2, sheetName = "淘汰赛奖励")
public class CrossRewardT {
	/** 排名 */
	@ExcelColumn(index = 0)
	public int rank;
	/** 奖励物品, 逗号分割每项，冒号分割奖励的id和数量 */
	@ExcelColumn(index = 1)
	public String items;
}
