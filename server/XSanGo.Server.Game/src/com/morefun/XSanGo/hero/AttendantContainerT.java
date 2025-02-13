/**
 * 
 */
package com.morefun.XSanGo.hero;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelComponet;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 随从容器模板
 * 
 * @author sulingyun
 * 
 */
@ExcelTable(fileName = "script/武将相关/缘分与随从脚本.xls", sheetName = "随从列表", beginRow = 2)
public class AttendantContainerT {
	@ExcelColumn(index = 0)
	public int heroId;

	@ExcelComponet(index = 3, columnCount = 5, size = 3)
	public AttendantT[] attendants;
}
