package com.morefun.XSanGo.crossServer;

import java.util.Date;

import com.morefun.XSanGo.script.DataFormat;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 比武大会
 * 
 * @author guofeng.qin
 */
@ExcelTable(fileName = "script/互动和聊天/比武大会.xls", beginRow = 2, sheetName = "跨服战参数")
public class TournamentConfigT {
	/** 资格赛开启时间 */
	@ExcelColumn(index = 0, dataType = DataFormat.DateTime)
	public Date qtStartDate;
	/** 资格赛截止时间 */
	@ExcelColumn(index = 1, dataType = DataFormat.DateTime)
	public Date qtEndDate;
	/** 每日挑战次数 */
	@ExcelColumn(index = 2)
	public int challengeCount;
	/** 资格赛初始积分 */
	@ExcelColumn(index = 3)
	public int startPoint;
	/** 每次挑战对手的CD时间 */
	@ExcelColumn(index = 4)
	public int cdTime;
	/** 每日刷新次数 */
	@ExcelColumn(index = 5)
	public int refreshCount;
	/** 重置时间 */
	@ExcelColumn(index = 6)
	public String resetTime;
	/** 战败获得积分 */
	@ExcelColumn(index = 7)
	public int failPoint;
	/** 资格赛战报显示数目 */
	@ExcelColumn(index = 8)
	public int figthRecordCount;
	/** 变更阵容开始时间 */
	@ExcelColumn(index = 9)
	public int changeFormationStartTime;
	/** 变更阵容结束时间 */
	@ExcelColumn(index = 10)
	public int changeFormationEndTime;
	/** 淘汰赛人数 */
	@ExcelColumn(index = 11)
	public int knockoutNum;
	/** 淘汰赛对战时间 */
	@ExcelColumn(index = 12)
	public String fightTime;
	/** 淘汰赛比赛间隔 */
	@ExcelColumn(index = 13)
	public int fightInterval;
	/** 场景编号 */
	@ExcelColumn(index = 14)
	public int stageBackIndex;
	/** 场景名称 */
	@ExcelColumn(index = 15)
	public String stageBackName;
	/** 比武大会报名开始时间 */
	@ExcelColumn(index = 16, dataType = DataFormat.DateTime)
	public Date signupStartTime;
	/** 比武大会报名结束时间 */
	@ExcelColumn(index = 17, dataType = DataFormat.DateTime)
	public Date signupEndTime;
	/** 第几届 */
	@ExcelColumn(index = 18)
	public int stageIndex;

	@ExcelColumn(index = 19, dataType = DataFormat.DateTime)
	public Date s32Start;
	@ExcelColumn(index = 20, dataType = DataFormat.DateTime)
	public Date s32End;
	@ExcelColumn(index = 21, dataType = DataFormat.DateTime)
	public Date s16Start;
	@ExcelColumn(index = 22, dataType = DataFormat.DateTime)
	public Date s16End;
	@ExcelColumn(index = 23, dataType = DataFormat.DateTime)
	public Date s8Start;
	@ExcelColumn(index = 24, dataType = DataFormat.DateTime)
	public Date s8End;
	/** 半绝赛开启时间 */
	@ExcelColumn(index = 25, dataType = DataFormat.DateTime)
	public Date shalfStart;
	@ExcelColumn(index = 26, dataType = DataFormat.DateTime)
	public Date shalfEnd;
	/** 绝赛开启时间 */
	@ExcelColumn(index = 27, dataType = DataFormat.DateTime)
	public Date sfinalStart;
	/** 绝赛截止时间 */
	@ExcelColumn(index = 28, dataType = DataFormat.DateTime)
	public Date sfinalEnd;
	/** 比武大会是否开启 */
	@ExcelColumn(index = 29)
	public int isOpen;
	/** 敬酒次数 */
	@ExcelColumn(index = 30)
	public String wineTime;

	/** 首胜次数 */
	@ExcelColumn(index = 31)
	public int winNum;

	/** 首胜奖励 */
	@ExcelColumn(index = 32)
	public String winNumItems;

	public int getWineTime(int stage) {
		int stageIndex = -1;
		switch (stage) {
		case 32:
			stageIndex = 0;
			break;
		case 16:
			stageIndex = 1;
			break;
		case 8:
			stageIndex = 2;
			break;
		case 4:
			stageIndex = 3;
			break;
		case 2:
			stageIndex = 4;
			break;
		}
		String[] timeArray = wineTime.split(",");
		if (stageIndex >= 0 && stageIndex < timeArray.length) {
			return Integer.parseInt(timeArray[stageIndex]);
		}
		return 0;
	}
}
