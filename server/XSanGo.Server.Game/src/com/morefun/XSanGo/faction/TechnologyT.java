package com.morefun.XSanGo.faction;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 公会科技列表
 * 
 * @author lixiongming
 *
 */
@ExcelTable(beginRow = 2, fileName = "script/互动和聊天/公会脚本.xls", sheetName = "公会科技列表")
public class TechnologyT {
	@ExcelColumn(index = 0)
	public int type;
	
	@ExcelColumn(index = 1)
	public int id;

	@ExcelColumn(index = 2)
	public int color;

	@ExcelColumn(index = 3)
	public String name;

	@ExcelColumn(index = 4)
	public String info;

	@ExcelColumn(index = 5)
	public int initLevel;

	@ExcelColumn(index = 6)
	public String icon;

	@ExcelColumn(index = 7)
	public String shortName;
	
	/** 英文属性代码 */
	@ExcelColumn(index = 8)
	public String code;
	
	@ExcelColumn(index = 9)
	public String logStr;
	
	@ExcelColumn(index = 10)
	public int isDisplay;
}
