package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * @author qinguofeng
 */
@ExcelTable(fileName = "script/活动和礼包/等级福利.xls", beginRow = 2, sheetName = "等级福利")
public class LevelWealConfigT {
	/**
	 * 等级区间-低
	 * */
	@ExcelColumn(index = 0)
	public int minLv;
	/**
	 * 等级区间-高
	 * */
	@ExcelColumn(index = 1)
	public int maxLv;
	/**
	 * 每天领取次数
	 * */
	@ExcelColumn(index = 2)
	public int expDayTime;
	/**
	 * 每天领取威望
	 * */
	@ExcelColumn(index = 3)
	public int expDayValue;
	/**
	 * 每次额外威望
	 * */
	@ExcelColumn(index = 4)
	public int expAddValue;
	/**
	 * 每天威望上限
	 * */
	@ExcelColumn(index = 5)
	public int expAddMax;
}
