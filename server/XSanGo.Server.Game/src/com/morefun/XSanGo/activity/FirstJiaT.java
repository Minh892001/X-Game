package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 第一佳
 * 
 * @author qinguofeng
 */
@ExcelTable(fileName = "script/活动和礼包/充值和消费.xls", sheetName = "第一佳鸡排", beginRow = 2)
public class FirstJiaT {
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
