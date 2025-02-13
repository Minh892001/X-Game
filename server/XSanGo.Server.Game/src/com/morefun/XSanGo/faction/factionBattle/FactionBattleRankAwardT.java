/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: FactionBattleRankAwardT
 * 功能描述：
 * 文件名：FactionBattleRankAwardT.java
 **************************************************
 */
package com.morefun.XSanGo.faction.factionBattle;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 公会战排行奖励
 * 
 * @author zwy
 * @since 2016-1-6
 * @version 1.0
 */
@ExcelTable(beginRow = 2, fileName = "script/互动和聊天/公会脚本.xls", sheetName = "公会战排名奖励")
public class FactionBattleRankAwardT {

	/** 编号 */
	@ExcelColumn(index = 0)
	public int id;

	/** 名次 */
	@ExcelColumn(index = 1)
	public String rank;

	/** 物品奖励 */
	@ExcelColumn(index = 2)
	public String items;

	/** 个人排行奖励 */
	@ExcelColumn(index = 7)
	public String personalItems;
}
