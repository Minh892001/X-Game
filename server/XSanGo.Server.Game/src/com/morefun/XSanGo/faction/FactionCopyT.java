package com.morefun.XSanGo.faction;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 公会副本关卡数据
 * 
 * @author lixiongming
 *
 */
@ExcelTable(beginRow = 2, fileName = "script/副本和怪物/公会副本.xls", sheetName = "关卡数据")
public class FactionCopyT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public String copyName;

	@ExcelColumn(index = 2)
	public int level;

	@ExcelColumn(index = 4)
	public int stageCount;// 场景总数

	@ExcelColumn(index = 5)
	public String tc;	//通关后公会所有会员都获得
	
	@ExcelColumn(index = 6)
	public int diff;// 难度
	
	@ExcelColumn(index = 10)
	public int vipAdditionLevel;// vip达到此值可额外增加1次挑战机会

	@ExcelColumn(index = 12)
	public int timeLimit;// 限时时长 分钟

	@ExcelColumn(index = 13)
	public String timeLimitTc;// 限时TC
	
	@ExcelColumn(index = 14)
	public int sumBlood;// 总血量
	
	@ExcelColumn(index = 15)
	public int sumGold;// 总金币
	
	@ExcelColumn(index = 16)
	public String warehouseTc;// 仓库掉落TC
	
	@ExcelColumn(index = 17)
	public int factionScore;// 公会积分
}
