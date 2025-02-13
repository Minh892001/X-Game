/**
 * 
 */
package com.morefun.XSanGo.battle;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 单挑技能
 * 
 * @author sulingyun
 *
 */
@ExcelTable(fileName = "script/技能数据/单挑技能.xls", sheetName = "单挑技能列表", beginRow = 2)
public class DuelSkillT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 6)
	public String target;

	@ExcelColumn(index = 8)
	public String effectProperty;

	@ExcelColumn(index = 9)
	public int effectValue;

	@ExcelColumn(index = 10)
	public int time;

	@ExcelColumn(index = 11)
	public String desc;

	@ExcelColumn(index = 12)
	public String effect;

}
