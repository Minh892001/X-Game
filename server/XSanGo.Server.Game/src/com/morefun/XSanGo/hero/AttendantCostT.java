package com.morefun.XSanGo.hero;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 重置随从产生的消耗
 * @author xiaojun.zhang
 *
 */
@ExcelTable(fileName = "script/武将相关/缘分与随从脚本.xls", sheetName = "重置随从参数", beginRow = 2)
public class AttendantCostT {

	@ExcelColumn(index = 0)
	public String itemId;

	@ExcelColumn(index = 1)
	public int num;

}
