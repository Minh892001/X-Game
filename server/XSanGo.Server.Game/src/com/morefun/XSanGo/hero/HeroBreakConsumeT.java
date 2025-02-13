/**
 * 
 */
package com.morefun.XSanGo.hero;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 武将突破所需条件配置
 * 
 * @author sulingyun
 *
 */
@ExcelTable(fileName = "script/武将相关/武将脚本.xls", sheetName = "突破配置", beginRow = 2)
public class HeroBreakConsumeT {
	@ExcelColumn(index = 0)
	public int breakLevel;

	@ExcelColumn(index = 1)
	public String prefix;

	@ExcelColumn(index = 2)
	public int jinbi;

	@ExcelColumn(index = 3)
	public int med3Count;

	@ExcelColumn(index = 4)
	public int drugCount;

	@ExcelColumn(index = 5)
	public int heroLevel;

	/** 解锁缘分索引 */
	@ExcelColumn(index = 6)
	public int unlockRelationIndex;

}
