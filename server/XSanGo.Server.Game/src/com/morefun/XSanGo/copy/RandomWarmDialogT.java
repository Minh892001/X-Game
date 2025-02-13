package com.morefun.XSanGo.copy;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/互动和聊天/副本PK脚本.xls", sheetName = "随机信息", beginRow = 2)
public class RandomWarmDialogT {
	@ExcelColumn(index = 1)
	public String msg;
}
