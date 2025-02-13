package com.morefun.XSanGo.treasure;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 寻宝矿难救援参数
 * 
 * @author lixiongming
 *
 */
@ExcelTable(fileName = "script/互动和聊天/寻宝大冒险脚本.xls", sheetName = "救援参数", beginRow = 2)
public class TreasureRescueT {
	@ExcelColumn(index = 0)
	public int level;

	@ExcelColumn(index = 1)
	public int color;

	@ExcelColumn(index = 2)
	public int rescuePeople;

	@ExcelColumn(index = 3)
	public String rescueTc;

	/** 援救成功后的额外奖励 */
	@ExcelColumn(index = 4)
	public String extraItems;

	/** 援救成功后被救援者奖励 */
	@ExcelColumn(index = 5)
	public String byRescueItems;

	/** 失败惩罚% */
	@ExcelColumn(index = 6)
	public int punishment;

	@ExcelColumn(index = 7)
	public int continueMinute;

	@ExcelColumn(index = 8)
	public int weight;
	
	/** 矿难特效 */
	@ExcelColumn(index = 9)
	public int effect;
}
