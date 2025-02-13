package com.morefun.XSanGo.partner;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(beginRow = 1, fileName = "script/武将相关/伙伴脚本.xls", sheetName = "配置参数")
public class PartnerLevelRequiredT {
	
	@ExcelColumn(index = 0)
	public int level;
	
}
