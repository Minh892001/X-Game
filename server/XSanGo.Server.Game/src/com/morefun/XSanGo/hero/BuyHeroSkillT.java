/**
 * 
 */
package com.morefun.XSanGo.hero;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 购买技能点配置模板
 * 
 * @author sulingyun
 * 
 */
@ExcelTable(fileName = "script/参数配置脚本/购买脚本.xls", sheetName = "技能配置", beginRow = 2)
public class BuyHeroSkillT {
	@ExcelColumn(index = 0)
	public int count;

	@ExcelColumn(index = 1)
	public int yuanbao;

	@ExcelColumn(index = 2)
	public int skillPoint;

}
