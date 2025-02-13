package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 全服活动时间限制
 * 
 * @author sunjie
 */
@ExcelTable(fileName = "script/活动和礼包/开服活动.xls", sheetName = "开服时间表", beginRow = 2)
public class AllServerActiveTimeLimitT {
	/**
	 * 开启时间1
	 * */
	@ExcelColumn(index = 0)
	public String openTime1;
	/**
	 * 结束时间2
	 * */
	@ExcelColumn(index = 1)
	public String openTime2;

}
