package com.morefun.XSanGo.role;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/参数配置脚本/新人引导脚本.xls", sheetName = "初始武将", beginRow = 2)
public class InitHeroT {

	@ExcelColumn(index = 0)
	public int defaultHero;

	@ExcelColumn(index = 2)
	public byte defaultHeroPos;

	@ExcelColumn(index = 4)
	public int firstFreeOnInTenHero;

	@ExcelColumn(index = 6)
	public byte firstFreeOnInTenHeroPos;

	@ExcelColumn(index = 7)
	public int firstFreeOnInHundredHero;

	@ExcelColumn(index = 9)
	public byte firstFreeOnInHundredHeroPos;

	@ExcelColumn(index = 11)
	public int roleSkillId;

}
