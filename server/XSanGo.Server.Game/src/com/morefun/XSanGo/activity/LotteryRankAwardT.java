/**
 * 
 */
package com.morefun.XSanGo.activity;

import java.util.LinkedHashMap;
import java.util.TreeMap;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 大富温 排名奖励
 */
@ExcelTable(fileName = "script/活动和礼包/大富翁活动脚本.xls", sheetName = "排名奖励", beginRow = 2)
public class LotteryRankAwardT {
	@ExcelColumn(index = 0)
	public int num;

	@ExcelColumn(index = 1)
	public int startRank;

	@ExcelColumn(index = 2)
	public int stopRank;

	@ExcelColumn(index = 3)
	public String items;
	
	public LinkedHashMap<String,Integer> itemMap = new LinkedHashMap<String,Integer>();

}
