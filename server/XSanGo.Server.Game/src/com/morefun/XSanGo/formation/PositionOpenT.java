/**
 * 
 */
package com.morefun.XSanGo.formation;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 阵位开启限制
 * 
 * @author sulingyun
 *
 */
@ExcelTable(fileName = "script/参数配置脚本/游戏参数配置表.xls", beginRow = 2, sheetName = "阵位等级限定")
public class PositionOpenT {
	@ExcelColumn(index = 0)
	public int postion;

	@ExcelColumn(index = 1)
	public int openLevel;
}
