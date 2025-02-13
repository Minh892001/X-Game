/**
 * 
 */
package com.morefun.XSanGo.activity;

import java.util.LinkedHashMap;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 大富温 巡回奖励
 */
@ExcelTable(fileName = "script/活动和礼包/大富翁活动脚本.xls", sheetName = "巡回奖励", beginRow = 2)
public class LotteryTourAwardT {
	@ExcelColumn(index = 0)
	public int num;

	@ExcelColumn(index = 1)
	public String item;
	
	public LinkedHashMap<String,Integer> itemMap = new LinkedHashMap<String,Integer>();
	
}
