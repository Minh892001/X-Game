/**
 * 
 */
package com.morefun.XSanGo.hero;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelComponet;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 武将技能模板
 * 
 * @author sulingyun
 *
 */
@ExcelTable(fileName = "script/技能数据/技能脚本.xls", sheetName = "技能配置", beginRow = 2)
public class HeroSkillT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public int type;

	@ExcelColumn(index = 2)
	public String name;

	@ExcelColumn(index = 7)
	public int studyLevel;

	/** 初始等级 */
	@ExcelColumn(index = 8)
	public int orignalLevel;

	@ExcelComponet(index = 14, columnCount = 4, size = 4)
	public SkillEffectT[] effectArray;
	
	/** 是否觉醒技能 */
	@ExcelColumn(index = 36)
	public int isAwakenSkill;
}
