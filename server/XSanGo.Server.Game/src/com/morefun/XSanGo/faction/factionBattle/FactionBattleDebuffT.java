/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: FactionBattleDebuffT
 * 功能描述：
 * 文件名：FactionBattleDebuffT.java
 **************************************************
 */
package com.morefun.XSanGo.faction.factionBattle;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 公会战DEBUFF数据
 * 
 * @author zwy
 * @since 2016-1-26
 * @version 1.0
 */
@ExcelTable(fileName = "script/互动和聊天/公会脚本.xls", sheetName = "疲惫状态表", beginRow = 2)
public class FactionBattleDebuffT {

	/** 等级 */
	@ExcelColumn(index = 0)
	public int debuffLvl;

	/** 值 */
	@ExcelColumn(index = 1)
	public int debuffValue;

	/** 描述 */
	@ExcelColumn(index = 2)
	public String debuffDesc;
}
