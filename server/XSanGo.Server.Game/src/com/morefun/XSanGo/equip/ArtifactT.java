/**
 * 
 */
package com.morefun.XSanGo.equip;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 神器
 */
@ExcelTable(fileName = "script/道具相关/神器脚本.xls", sheetName = "神器基础参数", beginRow = 2)
public class ArtifactT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public String name;
}
