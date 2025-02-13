/**
 * 
 */
package com.morefun.XSanGo.hero;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 武将修炼属性
 * 
 * @author lixiongming
 *
 */
@ExcelTable(fileName = "script/武将相关/武将修炼脚本.xls", sheetName = "属性列表", beginRow = 2)
public class PracticePropT {
	@ExcelColumn(index = 0)
	public int id;

	/** 英文属性名 */
	@ExcelColumn(index = 1)
	public String propName;

	@ExcelColumn(index = 2)
	public int color;

	/** 出现权值 */
	@ExcelColumn(index = 3)
	public int weight;

	@ExcelColumn(index = 4)
	public int maxLevel;

	@ExcelColumn(index = 5)
	public int baseValue;

	/** 每级提升属性百分比 */
	@ExcelColumn(index = 6)
	public int valuePercent;

	@ExcelColumn(index = 7)
	public int baseExp;

	/** 每级提升经验值百分比 */
	@ExcelColumn(index = 8)
	public int expPercent;
	
	/**功勋基础值*/
	@ExcelColumn(index = 9)
	public int baseExploit;
	
	/**每级提示功勋百分比*/
	@ExcelColumn(index = 10)
	public int exploitPercent;
}
