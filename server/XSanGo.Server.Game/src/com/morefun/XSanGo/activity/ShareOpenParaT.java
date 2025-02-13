/**
 * 
 */
package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 功能参数
 */
@ExcelTable(fileName = "script/活动和礼包/分享脚本.xls", sheetName = "功能参数", beginRow = 2)
public class ShareOpenParaT {
	@ExcelColumn(index = 0)
	public int openLvl;

	@ExcelColumn(index = 1)
	public String openIcon;
	
	@ExcelColumn(index = 2)
	public String serverIds;
	
}
