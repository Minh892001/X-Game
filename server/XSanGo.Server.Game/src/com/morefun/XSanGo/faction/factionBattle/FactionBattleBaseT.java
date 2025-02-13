/**************************************************
 * 上海美峰数码科技有限公司(http://www.morefuntek.com)
 * 模块名称: FactionBattleBaseT
 * 功能描述：
 * 文件名：FactionBattleBaseT.java
 **************************************************
 */
package com.morefun.XSanGo.faction.factionBattle;

import java.util.Date;

import com.morefun.XSanGo.script.DataFormat;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 公会战基本参数配置
 * 
 * @author zwy
 * @since 2016-1-4
 * @version 1.0
 */
@ExcelTable(fileName = "script/互动和聊天/公会脚本.xls", sheetName = "公会战基本参数", beginRow = 2)
public class FactionBattleBaseT {

	/** 场次编号 */
	@ExcelColumn(index = 0)
	public int id;

	/** 公会战需要公会等级 */
	@ExcelColumn(index = 1)
	public int needFactionLvl;

	/** 开启日期，每周几 */
	@ExcelColumn(index = 2)
	public String openWeek;

	/** 开启时间 */
	@ExcelColumn(index = 3, dataType = DataFormat.OnlyTime)
	public Date openTime;

	/** 参与角色等级 */
	@ExcelColumn(index = 4)
	public int needRoleLvl;

	/** 报名开始时间，已开启时间往后推的时间，单位:分 */
	@ExcelColumn(index = 5)
	public int enrollStartTime;

	/** 刷新阵营价格 */
	@ExcelColumn(index = 6)
	public int refreshCampPrice;

	/** 攻击等待时间 单位：秒 */
	@ExcelColumn(index = 8)
	public int attackWaitTime;

	/** 被攻击等待时间 单位：秒 */
	@ExcelColumn(index = 9)
	public int beAttackWaitTime;

	/** 复活时间 单位：秒 */
	@ExcelColumn(index = 10)
	public int reviveTime;

	/** 行军等待冷却时间 单位：秒 */
	@ExcelColumn(index = 11)
	public int marchCoolingTime;

	/** 立即结束行军冷却等待时间价格 */
	@ExcelColumn(index = 12)
	public int marchCdPrice;

	/** 行军单次涨价 */
	@ExcelColumn(index = 13)
	public int marchCdAddPrice;

	/** 挖宝等待冷却时间 单位：秒 */
	@ExcelColumn(index = 14)
	public int diggingTreasureCdTime;

	/** 战斗获胜减挖宝CD 单位：秒 */
	@ExcelColumn(index = 15)
	public int winMinusCdTime;

	/** 单次挖宝最大减CD次数 */
	@ExcelColumn(index = 16)
	public int maxMinusCd;

	/** 随机事件刷新间隔时间 单位：分 */
	@ExcelColumn(index = 17)
	public int randomEventCdTime;

	/** 占领系数 */
	@ExcelColumn(index = 18)
	public int occupationGain;

	/** 最大DEBUFF等级 */
	@ExcelColumn(index = 19)
	public int debuffMaxLvl;

	/** 持续时间 单位：分 */
	@ExcelColumn(index = 20)
	public int continueTime;

	/** 徽章 */
	@ExcelColumn(index = 21)
	public int badge;

	/** 胜利获取锦囊概率 */
	@ExcelColumn(index = 22)
	public int winGetItemChance;

	/** 失败获取锦囊概率 */
	@ExcelColumn(index = 23)
	public int loseGetItemChance;

	/** 场景 */
	@ExcelColumn(index = 24)
	public int sceneId;

	/** 场景名称 */
	@ExcelColumn(index = 25)
	public String sceneName;

	/** 个人排行数 */
	@ExcelColumn(index = 26)
	public int maxPersonalRankSize;

	/** 排行数 */
	@ExcelColumn(index = 27)
	public int maxRankSize;

	/** 战斗失败减挖宝CD 单位：秒 */
	@ExcelColumn(index = 28)
	public int failMinusCdTime;

	/** 几连胜触发特效 */
	@ExcelColumn(index = 29)
	public int evenKillParam;

	/** 战斗前置CD时间 秒 */
	@ExcelColumn(index = 37)
	public int frontCdTime;
	
	/** 机器人自动占领检测间隔 */
	@ExcelColumn(index = 38)
	public int robotCheckInterval;
}
