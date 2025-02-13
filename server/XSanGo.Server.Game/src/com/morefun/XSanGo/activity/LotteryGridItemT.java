/**
 * 
 */
package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 大富温 棋盘道具列表
 */
@ExcelTable(fileName = "script/活动和礼包/大富翁活动脚本.xls", sheetName = "棋盘道具列表", beginRow = 2)
public class LotteryGridItemT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public String item;

	@ExcelColumn(index = 2)
	public String itemName;

	@ExcelColumn(index = 3)
	public int lotteryType;

	@ExcelColumn(index = 4)
	public int num;

	@ExcelColumn(index = 5)
	public int pro;
	
	public int startRange;
	
	public int endRange;
}
