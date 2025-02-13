/**
 * 
 */
package com.morefun.XSanGo.hero;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 武将修炼将魂经验
 * 
 * @author lixiongming
 *
 */
@ExcelTable(fileName = "script/武将相关/武将修炼脚本.xls", sheetName = "将魂经验", beginRow = 2)
public class HeroExpT {
	@ExcelColumn(index = 0)
	public int color;

	@ExcelColumn(index = 1)
	public int exp;
}
