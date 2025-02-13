/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: FactionBattleAddAwardT
 * 功能描述：
 * 文件名：FactionBattleAddAwardT.java
 **************************************************
 */
package com.morefun.XSanGo.faction.factionBattle;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 公会战加成奖励
 * 
 * @author zwy
 * @since 2016-1-7
 * @version 1.0
 */
@ExcelTable(beginRow = 2, fileName = "script/互动和聊天/公会脚本.xls", sheetName = "公会战奖励浮动")
public class FactionBattleAddAwardT {

	/** 连胜次数 */
	@ExcelColumn(index = 0)
	public int num;

	/**
	 * 连胜增加徽章 基础徽章+连胜次数 连斩人数大于等于6人最多只获得15点徽章
	 */
	@ExcelColumn(index = 1)
	public int addBadge;

	/**
	 * 终结连胜增加徽章 基础徽章+对方连斩数量 击杀连斩大于等于6人的玩家。最多只获得15点徽章
	 */
	@ExcelColumn(index = 2)
	public int killAddBadge;

}
