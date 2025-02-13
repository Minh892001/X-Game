/**
 * 
 */
package com.morefun.XSanGo.copy;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 副本检测配置模板
 * 
 * @author sulingyun
 *
 */
@ExcelTable(fileName = "script/copy_battle_power_match.xls", sheetName = "copy_check")
public class CheckCopyT {
	@ExcelColumn(index = 0)
	public int copyId;

	@ExcelColumn(index = 2)
	public int minBattlePower;
}
