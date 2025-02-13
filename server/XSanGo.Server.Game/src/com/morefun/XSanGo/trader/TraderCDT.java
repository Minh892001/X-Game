/**
 * 
 */
package com.morefun.XSanGo.trader;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 召唤冷却时间
 */
@ExcelTable(fileName = "script/道具相关/商人脚本.xls", beginRow = 2, sheetName = "召唤冷却时间")
public class TraderCDT {
	@ExcelColumn(index = 0)
	public int vipLevel;

	@ExcelColumn(index = 1)
	public int minute;

}
