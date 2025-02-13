/**
 * 
 */
package com.morefun.XSanGo.hero;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 战斗力公式模板
 * 
 * @author sulingyun
 * 
 */
@ExcelTable(fileName = "script/参数配置脚本/属性对照表.xls", sheetName = "战斗力（评分）计算公式", beginRow = 2)
public class BattlePowerT {
	@ExcelColumn(index = 1)
	public String name;

	@ExcelColumn(index = 3)
	public String code;

	@ExcelColumn(index = 7)
	public float rank;
}
