/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: FactionBattleSceneT
 * 功能描述：
 * 文件名：FactionBattleSceneT.java
 **************************************************
 */
package com.morefun.XSanGo.faction.factionBattle;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 公会战场景数据
 * 
 * @author zwy
 * @since 2016-1-4
 * @version 1.0
 */
@ExcelTable(fileName = "script/互动和聊天/公会脚本.xls", sheetName = "公会战场景数据", beginRow = 2)
public class FactionBattleSceneT {

	/** 据点 */
	@ExcelColumn(index = 0)
	public int strongholdId;

	/**
	 * 类型 1.复活点和初始点 2.小型据点 3.中型据点 4.大型据点
	 */
	@ExcelColumn(index = 1)
	public byte type;

	/** 场景名称 */
	@ExcelColumn(index = 2)
	public String name;

	/** 场景图标 */
	@ExcelColumn(index = 3)
	public String icon;

	/** 连接点 */
	@ExcelColumn(index = 8)
	public String connectPoint;

	/** 据点增益归属 */
	@ExcelColumn(index = 9)
	public String homeCourt;

	/** 据点增益效果 */
	@ExcelColumn(index = 10)
	public String buffEffect;

	/** 据点怪物组数量 */
	@ExcelColumn(index = 11)
	public int monsterNum;

	/** 据点怪物组名次 */
	@ExcelColumn(index = 12)
	public String monsterName;
	
	/** 机器人自动占领间隔 */
	@ExcelColumn(index = 13)
	public int robotOccupySecond;
}
