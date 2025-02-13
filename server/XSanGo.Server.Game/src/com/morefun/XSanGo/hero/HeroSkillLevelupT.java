/**
 * 
 */
package com.morefun.XSanGo.hero;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelComponet;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 武将技能升级话费
 * 
 * @author sulingyun
 * 
 */
@ExcelTable(fileName = "script/技能数据/技能脚本.xls", sheetName = "技能升级花费", beginRow = 2)
public class HeroSkillLevelupT {
	@ExcelColumn(index = 0)
	public int level;

	@ExcelComponet(index = 1, columnCount = 2, size = 5)
	public HeroSkillLevelupCondition[] conditions;
}
