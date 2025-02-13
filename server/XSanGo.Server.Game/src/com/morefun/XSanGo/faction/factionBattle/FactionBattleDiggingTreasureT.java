/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: FactionBattleDiggingTreasureT
 * 功能描述：
 * 文件名：FactionBattleDiggingTreasureT.java
 **************************************************
 */
package com.morefun.XSanGo.faction.factionBattle;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 公会战挖宝掉落配置
 * 
 * @author zwy
 * @since 2016-1-4
 * @version 1.0
 */
@ExcelTable(fileName = "script/互动和聊天/公会脚本.xls", sheetName = "挖宝掉落", beginRow = 2)
public class FactionBattleDiggingTreasureT {

	/** 据点类型 */
	@ExcelColumn(index = 0)
	public byte type;

	/** 抽取TC次数 */
	@ExcelColumn(index = 1)
	public String tcAccount;

	/** 据点系数 */
	@ExcelColumn(index = 2)
	public int strongholdRatio;

	/** 掉落TC */
	@ExcelColumn(index = 3)
	public String tc;

	/** 基础粮草收益 */
	@ExcelColumn(index = 4)
	public int baseForage;
}
