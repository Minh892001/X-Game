/**
 * 
 */
package com.morefun.XSanGo.hero;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 怪物基础模板
 * 
 * @author sulingyun
 *
 */
@ExcelTable(fileName = "script/副本和怪物/怪物脚本.xls", sheetName = "怪物基础模版", beginRow = 2)
public class MonsterModeT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 17)
	public int duelSkill;

}
