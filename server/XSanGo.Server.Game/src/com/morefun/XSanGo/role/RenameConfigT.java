/**
 * 
 */
package com.morefun.XSanGo.role;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 改名配置
 * 
 * @author sulingyun
 *
 */
@ExcelTable(fileName = "script/参数配置脚本/主公脚本.xls", beginRow = 2, sheetName = "改名")
public class RenameConfigT {

	@ExcelColumn(index = 0)
	public int yuanbao;

	@ExcelColumn(index = 1)
	public int intervalDay;

}
