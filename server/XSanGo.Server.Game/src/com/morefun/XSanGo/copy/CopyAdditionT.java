/**
 * 
 */
package com.morefun.XSanGo.copy;

import com.morefun.XSanGo.item.PropertyT;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelComponet;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 副本额外奖励模板
 * 
 * @author sulingyun
 * 
 */
@ExcelTable(fileName = "script/参数配置脚本/主公脚本.xls", sheetName = "扫荡额外奖励", beginRow = 2)
public class CopyAdditionT {
	@ExcelColumn(index = 0)
	public int level;

	@ExcelComponet(index = 1, columnCount = 2, size = 2)
	public PropertyT[] items;

}
