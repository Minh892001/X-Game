package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * @author qinguofeng
 */
@ExcelTable(fileName = "script/活动和礼包/幸运大转盘.xls", sheetName = "抽奖上限设定", beginRow = 2)
public class FortuneWheelVipT {

	/**
	 * vip等级
	 * */
	@ExcelColumn(index = 0)
	public int vipLv;
	/**
	 * 次数上限
	 * */
	@ExcelColumn(index = 1)
	public int maxCount;
}
