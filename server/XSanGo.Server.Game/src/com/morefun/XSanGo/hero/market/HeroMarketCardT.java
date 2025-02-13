/**
 * 
 */
package com.morefun.XSanGo.hero.market;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 抽卡模板
 * 
 * @author sulingyun
 * 
 */
@ExcelTable(fileName = "script/武将相关/抽卡脚本.xls", beginRow = 2)
public class HeroMarketCardT {
	@ExcelColumn(index = 0)
	public String templateId;

	@ExcelColumn(index = 1)
	public String name;

	@ExcelColumn(index = 2)
	public int minCount;

	@ExcelColumn(index = 3)
	public int maxCount;

	@ExcelColumn(index = 4)
	public int rate;

	@ExcelColumn(index = 5)
	public int dropInFirst;

	@ExcelColumn(index = 6)
	public int fixedDropInContinuous;

	@ExcelColumn(index = 7)
	public int distinctInContinuous;
}
