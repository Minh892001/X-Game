/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: FactionBattleMessageT
 * 功能描述：
 * 文件名：FactionBattleMessageT.java
 **************************************************
 */
package com.morefun.XSanGo.faction.factionBattle;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 公会战提示信息
 * 
 * @author zwy
 * @since 2016-1-5
 * @version 1.0
 */
@ExcelTable(fileName = "script/互动和聊天/公会脚本.xls", sheetName = "公会战提示信息", beginRow = 2)
public class FactionBattleMessageT {

	/** 消息编号 */
	@ExcelColumn(index = 0)
	public String msgId;

	/** 消息内容 */
	@ExcelColumn(index = 1)
	public String content;

	/** 次数 */
	@ExcelColumn(index = 2)
	public int times;
}
