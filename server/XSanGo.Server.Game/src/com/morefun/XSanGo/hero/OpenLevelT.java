/**
 * 
 */
package com.morefun.XSanGo.hero;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 武将修炼开放等级
 * 
 * @author lixiongming
 *
 */
@ExcelTable(fileName = "script/武将相关/武将修炼脚本.xls", sheetName = "属性开放等级", beginRow = 2)
public class OpenLevelT {
	@ExcelColumn(index = 0)
	public int index;

	@ExcelColumn(index = 1)
	public int level;
}
