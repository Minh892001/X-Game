/**
 * 
 */
package com.morefun.XSanGo.achieve;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 成就分类列表
 */
@ExcelTable(fileName = "script/成就脚本/成就脚本.xls", sheetName = "分类列表", beginRow = 2)
public class AchieveCataLogT {
	/**
	 * 系统ID
	 */
	@ExcelColumn(index = 0)
	public int functionId;
	
	/**
	 * 成就类型
	 */
	@ExcelColumn(index = 2)
	public String type;
	
	/**是否特殊类型 */
	@ExcelColumn(index = 3)
	public int isSpecial;
	
}
