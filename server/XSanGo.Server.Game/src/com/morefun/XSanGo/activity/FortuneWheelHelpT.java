package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * @author qinguofeng
 */
@ExcelTable(fileName = "script/活动和礼包/幸运大转盘.xls", sheetName = "界面规则", beginRow = 2)
public class FortuneWheelHelpT {
	/**
	 * 编号
	 * */
	@ExcelColumn(index = 0)
	public int id;
	/**
	 * 帮助内容
	 * */
	@ExcelColumn(index = 1)
	public String content;
}
