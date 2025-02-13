package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 战斗力嘉奖
 * @author qinguofeng
 */
@ExcelTable(fileName = "script/活动和礼包/充值和消费.xls", sheetName = "战斗力嘉奖", beginRow = 2)
public class PowerRewardT {
	/**
	 * 对应战力
	 * */
	@ExcelColumn(index = 0)
	public int power;
	/**
	 * 奖励宝箱ID
	 * */
	@ExcelColumn(index = 1)
	public String rewardTemplateId;
}
