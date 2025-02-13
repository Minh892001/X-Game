/**
 * 
 */
package com.morefun.XSanGo.hero.market;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 限时武将道具列表
 * 
 * @author lixiongming
 *
 */
@ExcelTable(fileName = "script/武将相关/抽卡脚本.xls", sheetName = "魂匣道具", beginRow = 2)
public class LimitHeroItemT {
	@ExcelColumn(index = 0)
	public int index;

	@ExcelColumn(index = 1)
	public String itemId;

	@ExcelColumn(index = 2)
	public int minNum;

	@ExcelColumn(index = 3)
	public int maxNum;

	/** 权值 */
	@ExcelColumn(index = 4)
	public int weight;

}
