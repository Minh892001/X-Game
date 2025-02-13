package com.morefun.XSanGo.colorfulEgg;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/活动和礼包/砸蛋配置表.xls", beginRow = 2, sheetName="奖励控制")
public class EggRewardConfT {
	/** 配置id*/
	@ExcelColumn(index = 0)
	public int id;
	/** 道具id*/
	@ExcelColumn(index = 1)
	public String itemId;
	/**第二次相同概率*/
	@ExcelColumn(index = 2)
	public int pro2;
	/**第3次相同概率*/
	@ExcelColumn(index = 3)
	public int pro3;
	/**最小数量*/
	@ExcelColumn(index = 4)
	public int minNum;
	/**最大数量*/
	@ExcelColumn(index = 5)
	public int maxNum;
}
