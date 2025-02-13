package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 成长基金奖励配置
 * 
 * @author qinguofeng
 */
@ExcelTable(fileName = "script/活动和礼包/成长基金.xls", sheetName = "奖励和限制", beginRow = 2)
public class FundT {
	/**
	 * 领奖等级
	 * */
	@ExcelColumn(index = 0)
	public int level;

	/**
	 * 奖励元宝数量
	 * */
	@ExcelColumn(index = 1)
	public int rmby;
}
