package com.morefun.XSanGo.ladder;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/互动和聊天/群雄争霸脚本.xls", beginRow = 2, sheetName = "基本参数")
public class LadderInitT {
	// 开启等级
	@ExcelColumn(index = 0)
	public int level;

	// 战报显示数量
	@ExcelColumn(index = 1)
	public int reportNumber;

	// 排行榜显示数量
	@ExcelColumn(index = 2)
	public int rankNum;

	// 全部排行
	@ExcelColumn(index = 3)
	public int rankNumAll;

	// 每日重置挑战次数
	@ExcelColumn(index = 4)
	public String interval;

	// 每日赠送挑战次数
	@ExcelColumn(index = 5)
	public int challengeNum;

	// 初始等级
	@ExcelColumn(index = 6)
	public int initLevel;

	// 初始星级
	@ExcelColumn(index = 7)
	public int initStar;

	// 连胜场次
	@ExcelColumn(index = 8)
	public int winNum;

	// 连胜获得星数
	@ExcelColumn(index = 9)
	public int winStar;

	// 降级
	@ExcelColumn(index = 10)
	public int degrade;

	// 发送奖励的日期
	@ExcelColumn(index = 11)
	public int rewardDate;

	// 发送奖励的开始时间
	@ExcelColumn(index = 12)
	public String rewardStarTime;

	// 发送奖励的结束时间
	@ExcelColumn(index = 13)
	public String rewardEndTime;

	// 赛季开启日期
	@ExcelColumn(index = 14)
	public int season;

	// 赛季 间隔
	@ExcelColumn(index = 15)
	public int seasonInterval;

	/**匹配对手排名范围,分割 前面是高于自己后面是低于自己*/
	@ExcelColumn(index = 16)
	public String rivalRange;

	/** 扣星保护等级*/
	@ExcelColumn(index = 19)
	public int deductStarLevel;

	/** 匹配机器人等级*/
	@ExcelColumn(index = 20)
	public int matchRobotLevel;
	
	/** 无连胜等级*/
	@ExcelColumn(index = 21)
	public int noWin2Level;
}
