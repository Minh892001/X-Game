/**
 * 
 */
package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 大富温 棋盘方格配置
 */
@ExcelTable(fileName = "script/活动和礼包/大富翁活动脚本.xls", sheetName = "棋盘方格配置", beginRow = 2)
public class LotteryGridT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public int pointType;

	@ExcelColumn(index = 2)
	public int pro;

}
