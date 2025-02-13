package com.morefun.XSanGo.hero;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 重置随从配置对象
 * @author xiaojun.zhang
 *
 */
@ExcelTable(fileName = "script/武将相关/缘分与随从脚本.xls", sheetName = "出现概率", beginRow = 2)
public class ResetAttendantT {
	
	@ExcelColumn(index = 0)
	public int itemId;

	@ExcelColumn(index = 1)
	public String heroName;
	
	@ExcelColumn(index = 2)
	public int color;
	
	@ExcelColumn(index = 3)
	public int weight;
}
