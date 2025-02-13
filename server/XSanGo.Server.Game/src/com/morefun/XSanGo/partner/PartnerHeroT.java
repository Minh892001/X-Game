package com.morefun.XSanGo.partner;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(beginRow = 2, fileName = "script/武将相关/伙伴脚本.xls", sheetName = "武将列表")
public class PartnerHeroT {
	
	@ExcelColumn(index = 0)
	public int id;
	@ExcelColumn(index = 1)
	public String heroName;
	@ExcelColumn(index = 2)
	public int color;
	@ExcelColumn(index = 3)
	public int weight;
}
