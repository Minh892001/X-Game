package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * @author qinguofeng
 */
@ExcelTable(fileName = "script/活动和礼包/幸运大转盘.xls", sheetName = "道具分类", beginRow = 2)
public class FortuneWheelRewardTypeT {

	/**
	 * 类型
	 * */
	@ExcelColumn(index = 0)
	public int type;
	/**
	 * 开始时间
	 * */
	@ExcelColumn(index = 1)
	public String startTime;
	/**
	 * 结束时间
	 * */
	@ExcelColumn(index = 2)
	public String endTime;
}
