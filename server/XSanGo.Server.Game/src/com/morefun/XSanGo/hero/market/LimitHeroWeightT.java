/**
 * 
 */
package com.morefun.XSanGo.hero.market;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 限时武将概率配置
 * @author lixiongming
 *
 */
@ExcelTable(fileName = "script/武将相关/抽卡脚本.xls",sheetName="魂匣概率", beginRow = 2)
public class LimitHeroWeightT {
	@ExcelColumn(index = 0)
	public int index;

	/**周武将概率*/
	@ExcelColumn(index = 1)
	public int weekHeroWeight;

	/**周将魂概率*/
	@ExcelColumn(index = 2)
	public int weekSoulWeight;

	/**今日武将概率*/
	@ExcelColumn(index = 3)
	public int todayHeroWeight;

	/**今日将魂概率*/
	@ExcelColumn(index = 4)
	public int todaySoulWeight;
	
	/**物品概率*/
	@ExcelColumn(index = 5)
	public int itemWeight;
	
}
