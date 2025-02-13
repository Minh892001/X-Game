/**
 * 
 */
package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 大富温 摇骰元宝
 */
@ExcelTable(fileName = "script/活动和礼包/大富翁活动脚本.xls", sheetName = "摇骰元宝", beginRow = 2)
public class LotteryTimeCostT {
	@ExcelColumn(index = 0)
	public int numRoll;

	@ExcelColumn(index = 1)
	public int coinType;

	@ExcelColumn(index = 2)
	public int cost;
}
