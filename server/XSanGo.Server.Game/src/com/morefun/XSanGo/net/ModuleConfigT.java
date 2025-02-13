/**
 * 
 */
package com.morefun.XSanGo.net;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * @author sulingyun
 * 
 */
@ExcelTable(fileName = "程序配置.xls", sheetName = "模块配置表")
public class ModuleConfigT {
	@ExcelColumn(index = 0)
	public String moduleName;

	@ExcelColumn(index = 1)
	public String className;
}
