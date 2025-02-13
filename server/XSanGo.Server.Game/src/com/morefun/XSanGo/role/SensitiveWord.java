package com.morefun.XSanGo.role;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/敏感字库.xls", sheetName = "敏感字库")
public class SensitiveWord {
	@ExcelColumn(index = 0)
	public String word;
}
