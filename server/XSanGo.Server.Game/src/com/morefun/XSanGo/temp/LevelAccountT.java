/**
 * 
 */
package com.morefun.XSanGo.temp;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 冲级奖励的帐号列表
 * 
 * @author sulingyun
 *
 */
@ExcelTable(fileName = "script/封测返利.xls", sheetName = "冲级奖励")
public class LevelAccountT {
	@ExcelColumn(index = 0)
	public String account;
}
