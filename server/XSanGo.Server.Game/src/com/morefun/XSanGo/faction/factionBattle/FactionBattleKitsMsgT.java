/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: FactionBattleKitsMsgT
 * 功能描述：
 * 文件名：FactionBattleKitsMsgT.java
 **************************************************
 */
package com.morefun.XSanGo.faction.factionBattle;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 锦囊获取提示
 * 
 * @author zwy
 * @since 2016-1-14
 * @version 1.0
 */
@ExcelTable(fileName = "script/互动和聊天/公会脚本.xls", sheetName = "锦囊获取提示", beginRow = 2)
public class FactionBattleKitsMsgT {

	/**
	 * 获取途径 1.首次进入战场 2.战斗胜利获得 3.战斗失败复活后获得
	 */
	@ExcelColumn(index = 0)
	public byte type;

	/** 内容 */
	@ExcelColumn(index = 1)
	public String content;
}
