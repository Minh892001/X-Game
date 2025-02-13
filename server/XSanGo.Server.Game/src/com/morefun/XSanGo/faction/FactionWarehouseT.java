package com.morefun.XSanGo.faction;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 公会仓库
 * 
 * @author lixiongming
 *
 */
@ExcelTable(beginRow = 2, fileName = "script/互动和聊天/公会脚本.xls", sheetName = "公会仓库")
public class FactionWarehouseT {
	@ExcelColumn(index = 0)
	public int level;

	@ExcelColumn(index = 1)
	public int volume;
}
