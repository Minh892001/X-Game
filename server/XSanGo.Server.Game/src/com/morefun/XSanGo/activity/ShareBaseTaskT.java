/**
 * 
 */
package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 分享任务列表
 */
@ExcelTable(fileName = "script/活动和礼包/分享脚本.xls", sheetName = "分享任务列表", beginRow = 2)
public class ShareBaseTaskT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public String title;

	@ExcelColumn(index = 2)
	public String content;
	
	@ExcelColumn(index = 3)
	public String target;
	
	@ExcelColumn(index = 4)
	public String demand;
	
	@ExcelColumn(index = 5)
	public String icon;
	
	@ExcelColumn(index = 6)
	public int needLvl;

}
