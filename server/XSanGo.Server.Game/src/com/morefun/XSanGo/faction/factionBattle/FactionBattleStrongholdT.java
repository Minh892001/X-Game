/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: FactionBattleStrongholdT
 * 功能描述：
 * 文件名：FactionBattleStrongholdT.java
 **************************************************
 */
package com.morefun.XSanGo.faction.factionBattle;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelComponet;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 公会战据点负载数据
 * 
 * @author zwy
 * @since 2016-1-4
 * @version 1.0
 */
@ExcelTable(fileName = "script/互动和聊天/公会脚本.xls", sheetName = "据点负载", beginRow = 2)
public class FactionBattleStrongholdT {

	/** 据点类型 */
	@ExcelColumn(index = 0)
	public byte type;

	/** 负载人数 */
	@ExcelColumn(index = 1)
	public int loadRoleNum;

	/** 状态 */
	@ExcelComponet(index = 2, columnCount = 3, size = 3)
	public StrongholdStateT[] states;
}
