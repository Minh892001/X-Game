/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: FactionBattleRandomEventT
 * 功能描述：
 * 文件名：FactionBattleRandomEventT.java
 **************************************************
 */
package com.morefun.XSanGo.faction.factionBattle;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 随机事件
 * 
 * @author zwy
 * @since 2016-1-14
 * @version 1.0
 */
@ExcelTable(fileName = "script/互动和聊天/公会脚本.xls", sheetName = "随机事件表", beginRow = 2)
public class FactionBattleRandomEventT {

	/** 事件ID */
	@ExcelColumn(index = 0)
	public int eventId;

	/** 事件类型 */
	@ExcelColumn(index = 1)
	public byte eventType;

	/** 事件触发TC */
	@ExcelColumn(index = 2)
	public String tc;

	/** 挖宝随机概率 */
	@ExcelColumn(index = 3)
	public int diggingtreasureRatio;

	/** 下次概率增加 */
	@ExcelColumn(index = 4)
	public int addRatio;

	/** 出现的据点类型 */
	@ExcelColumn(index = 5)
	public String strongholdType;

	/** 权值 */
	@ExcelColumn(index = 6)
	public int weight;

	/** 公告类型 */
	@ExcelColumn(index = 7)
	public byte noticeype;

	/** 图标 */
	@ExcelColumn(index = 8)
	public String icon;

	/** 是否开放 */
	@ExcelColumn(index = 9)
	public byte isOpen;
}
