/**
 * 
 */
package com.morefun.XSanGo.hero;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * @author sulingyun
 * 
 */
@ExcelTable(fileName = "script/武将相关/武将脚本.xls", beginRow = 2)
public class HeroLevelupExpT {
	@ExcelColumn(index = 0)
	public int level;
	
	@ExcelColumn(index = 1)
	public int exp;
	
	@ExcelColumn(index = 2)
	public int rewardExp;	
}
