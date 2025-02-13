package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * @author zhouming
 */
@ExcelTable(fileName = "script/活动和礼包/百步穿杨.xls", sheetName = "抽奖获得", beginRow = 2)
public class ShootSpecialRewardT {

	/**
	 * 类型
	 */
	@ExcelColumn(index = 0)
	public int Type;

	/**
	 * 数量
	 */
	@ExcelColumn(index = 1)
	public int Number;

	/**
	 * 物品
	 */
	@ExcelColumn(index = 2)
	public int Index;
}
