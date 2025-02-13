/**
 * 
 */
package com.morefun.XSanGo.role;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 主公头像边框
 */
@ExcelTable(fileName = "script/参数配置脚本/主公脚本.xls", sheetName = "头像框", beginRow = 2)
public class HeadBorderT {

	@ExcelColumn(index = 0)
	public int Id;

	@ExcelColumn(index = 1)
	public String headBorder;

	@ExcelColumn(index = 2)
	public int desc;
}
