/**
 * 
 */
package com.morefun.XSanGo.hero;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 武将星级配置模板
 * 
 * @author sulingyun
 * 
 */
@ExcelTable(fileName = "script/武将相关/武将脚本.xls", sheetName = "升星配置", beginRow = 2)
public class HeroStarT {
	@ExcelColumn(index = 0)
	public int color;
	
	@ExcelColumn(index = 1)
	public int star;

	@ExcelColumn(index = 2)
	public int open;

	@ExcelColumn(index = 3)
	public int soulCount;

	@ExcelColumn(index = 4)
	public int jinbi;
}
