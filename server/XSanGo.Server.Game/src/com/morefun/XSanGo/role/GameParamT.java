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
@ExcelTable(fileName = "script/参数配置脚本/游戏参数配置表.xls", sheetName = "系统参数配置", beginRow = 2)
public class GameParamT {
	@ExcelColumn(index = 0)
	public String id;

	@ExcelColumn(index = 1)
	public String function;

	@ExcelColumn(index = 2)
	public String value;

	@ExcelColumn(index = 3)
	public String xplain;
}
