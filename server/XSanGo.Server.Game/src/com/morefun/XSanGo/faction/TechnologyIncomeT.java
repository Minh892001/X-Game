package com.morefun.XSanGo.faction;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelComponet;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 公会科技收益
 * 
 * @author lixiongming
 *
 */
@ExcelTable(beginRow = 2, fileName = "script/互动和聊天/公会脚本.xls", sheetName = "科技收益")
public class TechnologyIncomeT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelComponet(index = 1, size = 20, columnCount = 1)
	public TechnologyLevelValueT[] addValues;
}
