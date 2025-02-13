/**
 * 
 */
package com.morefun.XSanGo.hero;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 进阶消耗和奖励配置
 * 
 * @author sulingyun
 * 
 */
@ExcelTable(fileName = "script/武将相关/武将脚本.xls", sheetName = "进阶消耗奖励", beginRow = 2)
public class HeroQualityProduceConsumeT {

	@ExcelColumn(index = 1)
	public int quality;

	@ExcelColumn(index = 3)
	public int jinbi;

	@ExcelColumn(index = 4)
	public int med1Count;

	@ExcelColumn(index = 5)
	public int level;

	@ExcelColumn(index = 6)
	public String tc;
}
