package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 累计登录 整个周期一次性奖励
 * 
 * @author sunjie
 */
@ExcelTable(fileName = "script/活动和礼包/充值和消费.xls", sheetName = "永久登录有礼", beginRow = 2)
public class DayforverLoginRewardT {
	/**
	 * 对应天数
	 * */
	@ExcelColumn(index = 0)
	public int day;
	/**
	 * 奖励宝箱ID
	 * */
	@ExcelColumn(index = 1)
	public String rewardTemplateId;
}
