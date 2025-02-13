/**
 * 
 */
package com.morefun.XSanGo.role;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * @author sulingyun
 * 
 */
@ExcelTable(fileName = "script/参数配置脚本/主公脚本.xls", beginRow = 2)
public class RoleLevelConfigT {
	@ExcelColumn(index = 0)
	public int level;
	@ExcelColumn(index = 1)
	public int exp;
	@ExcelColumn(index = 2)
	public int herolvMax;
	@ExcelColumn(index = 3)
	public int strengMax;

	@ExcelColumn(index = 4)
	public int junlingLimit;

	@ExcelColumn(index = 5)
	public int junlingRecovery;
}
