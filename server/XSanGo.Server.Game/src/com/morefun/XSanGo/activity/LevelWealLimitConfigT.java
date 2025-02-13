package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * @author qinguofeng
 */
@ExcelTable(fileName = "script/活动和礼包/等级福利.xls", beginRow = 2, sheetName = "参数配置")
public class LevelWealLimitConfigT {
	/**
	 * 等级下限
	 * */
	@ExcelColumn(index = 0)
	public int startLv;
}
