package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 冲级有奖
 * 
 * @author qinguofeng
 */
@ExcelTable(fileName = "script/活动和礼包/充值和消费.xls", sheetName = "等级礼包", beginRow = 2)
public class LevelRewardT {
	/**
	 * 对应等级
	 * */
	@ExcelColumn(index = 0)
	public int level;
	/**
	 * 奖励宝箱ID
	 * */
	@ExcelColumn(index = 1)
	public String rewardTemplateId;
}
