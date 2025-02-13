package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 成长基金基本配置
 * 
 * @author qinguofeng
 */
@ExcelTable(fileName = "script/活动和礼包/成长基金.xls", sheetName = "基金内容", beginRow = 2)
public class FundConfigT {
	/**
	 * 购买价格
	 * */
	@ExcelColumn(index = 2)
	public int price;
	/**
	 * 购买需要的Vip最低等级
	 * */
	@ExcelColumn(index = 3)
	public int minVip;
}
