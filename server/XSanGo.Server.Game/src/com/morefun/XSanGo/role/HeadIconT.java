/**
 * 
 */
package com.morefun.XSanGo.role;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 活动列表
 */
@ExcelTable(fileName = "script/参数配置脚本/主公脚本.xls", sheetName = "系统头像", beginRow = 2)
public class HeadIconT {
	
	@ExcelColumn(index = 0)
	public int Id;
	
	@ExcelColumn(index = 1)
	public String Headicon;

	@ExcelColumn(index = 2)
	public int sex;
	
	@ExcelColumn(index = 3)
	public String desc;
	
	@ExcelColumn(index = 4)
	public int bornGet;
}
