package com.morefun.XSanGo.colorfulEgg;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/活动和礼包/砸蛋配置表.xls", beginRow = 2, sheetName="基础开关")
public class EggBasisConfT {
	/** 等级要求*/
	@ExcelColumn(index = 0)
	public int limitLevel;
	/** 功能入口标记    登录弹出或者用户操作弹出*/
	@ExcelColumn(index = 1)
	public int entryFlag;
	/** 或者物品标记 0-三选一  1-全部获取*/
	@ExcelColumn(index = 2)
	public int rewardFlag;
	/** 彩蛋1使用的随机奖池标记*/
	@ExcelColumn(index = 3)
	public int firstEggRewardFlag;
	/** 彩蛋2使用的随机奖池标记*/
	@ExcelColumn(index = 4)
	public int secondEggRewardFlag;
	/** 彩蛋3使用的随机奖池标记*/
	@ExcelColumn(index = 5)
	public int thirdEggRewardFlag;
	/** 彩蛋1免费领取时间间隔 -1表示没有CD间隔*/
	@ExcelColumn(index = 6)
	public int freeCD1;
	/** 彩蛋2免费领取时间间隔 -1表示没有CD间隔*/
	@ExcelColumn(index = 7)
	public int freeCD2;
	/** 彩蛋3免费领取时间间隔 -1表示没有CD间隔*/
	@ExcelColumn(index = 8)
	public int freeCD3;
	/**刷新时间*/
	@ExcelColumn(index = 9)
	public String refreshTime;
	/**最大参与次数*/
	@ExcelColumn(index = 10)
	public int limitNum;
}
