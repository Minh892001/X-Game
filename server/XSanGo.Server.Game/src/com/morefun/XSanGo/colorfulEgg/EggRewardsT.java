package com.morefun.XSanGo.colorfulEgg;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/活动和礼包/砸蛋配置表.xls", beginRow = 2, sheetName="奖池")
public class EggRewardsT {
	/** 配置id*/
	@ExcelColumn(index = 0)
	public int id;
	/** 奖池类型*/
	@ExcelColumn(index = 1)
	public int type;
	/** 奖励道具Id*/
	@ExcelColumn(index = 2)
	public String itemId;
	/** 最小随机数量*/
	@ExcelColumn(index = 3)
	public int minNum;
	/** 最大随机数量*/
	@ExcelColumn(index = 4)
	public int maxNum;
	/** 权重*/
	@ExcelColumn(index = 5)
	public int weight;

}
