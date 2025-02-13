/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: FactionBattlePersonalRankAwardT
 * 功能描述：
 * 文件名：FactionBattlePersonalRankAwardT.java
 **************************************************
 */
package com.morefun.XSanGo.faction.factionBattle;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 个人排行奖励配置
 * 
 * @author weiyi.zhao
 * @since 2016-5-10
 * @version 1.0
 */
@ExcelTable(beginRow = 2, fileName = "script/互动和聊天/公会脚本.xls", sheetName = "公会战个人排名奖励系数")
public class FactionBattlePersonalRankAwardT {

	/** 排名 */
	@ExcelColumn(index = 0)
	public String rank;

	/** 奖励系数 */
	@ExcelColumn(index = 1)
	public int scale;
}
