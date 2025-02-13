package com.morefun.XSanGo.template;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(beginRow = 2, fileName = "script/跨服竞技场.xls", sheetName = "跨服区间")
public class CrossArenaRangeT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public int beginServerId;

	@ExcelColumn(index = 2)
	public int endServerId;
}
