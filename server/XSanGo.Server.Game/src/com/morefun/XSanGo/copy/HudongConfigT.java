package com.morefun.XSanGo.copy;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/参数配置脚本/游戏参数配置表.xls", sheetName = "关卡互动参数", beginRow = 2)
public class HudongConfigT {
	@ExcelColumn(index = 0)
	public int hudongCount;

	@ExcelColumn(index = 1)
	public int worshipCount;
}
