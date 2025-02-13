/**
 * 
 */
package com.morefun.XSanGo.achieve;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 成就进度奖励
 */
@ExcelTable(fileName = "script/成就脚本/成就脚本.xls", sheetName = "进度奖励", beginRow = 2)
public class AchieveProAwardT {
	/**成就编号 */
	@ExcelColumn(index = 0)
	public int id;
	
	/**进度 */
	@ExcelColumn(index = 1)
	public int progress;
	
	/**道具ID */
	@ExcelColumn(index = 2)
	public String item1;
	
	/**道具数量 */
	@ExcelColumn(index = 3)
	public int itemNum1;
	
	/**道具ID */
	@ExcelColumn(index = 4)
	public String item2;
	
	/**道具数量 */
	@ExcelColumn(index = 5)
	public int itemNum2;
	
	/**道具ID */
	@ExcelColumn(index = 6)
	public String item3;
	
	/**道具数量 */
	@ExcelColumn(index = 7)
	public int itemNum3;
	
	/**道具ID */
	@ExcelColumn(index = 8)
	public String item4;
	
	/**道具数量 */
	@ExcelColumn(index = 9)
	public int itemNum4;
	
	/**道具ID */
	@ExcelColumn(index = 10)
	public String item5;
	
	/**道具数量 */
	@ExcelColumn(index = 11)
	public int itemNum5;
	
	/**头像说明*/
	@ExcelColumn(index = 12)
	public String tipsTx;
	
	public Map<String,Integer> itemMap = new LinkedHashMap<String,Integer>();
}
