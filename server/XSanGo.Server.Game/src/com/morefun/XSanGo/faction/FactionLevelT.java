package com.morefun.XSanGo.faction;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(beginRow = 2, fileName = "script/互动和聊天/公会脚本.xls", sheetName = "公会等级")
public class FactionLevelT {
	@ExcelColumn(index = 0)
	public int level;

	@ExcelColumn(index = 1)
	public int exp;

	@ExcelColumn(index = 2)
	public int people;// 人数
	
	@ExcelColumn(index = 3)
	public int elder;// 长老数量
}
