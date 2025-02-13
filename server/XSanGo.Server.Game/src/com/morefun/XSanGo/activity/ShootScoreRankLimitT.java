package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * @author zhouming
 */
@ExcelTable(fileName = "script/活动和礼包/百步穿杨.xls", sheetName = "入榜积分", beginRow = 2)
public class ShootScoreRankLimitT {

	/**
	 * 编号
	 */
	@ExcelColumn(index = 0)
	public int ID;

	/**
	 * 排名开始
	 */
	@ExcelColumn(index = 1)
	public int Start;

	/**
	 * 排名结束
	 */
	@ExcelColumn(index = 2)
	public int Closure;

	/**
	 * 入榜积分
	 */
	@ExcelColumn(index = 3)
	public int Integration;
}
