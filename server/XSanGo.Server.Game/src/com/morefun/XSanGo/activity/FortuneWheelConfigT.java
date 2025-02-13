package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * @author qinguofeng
 */
@ExcelTable(fileName = "script/活动和礼包/幸运大转盘.xls", sheetName = "参数配置", beginRow = 2)
public class FortuneWheelConfigT {

	/**
	 * 获取抽奖次数的条件(需要充值的金额)
	 * */
	@ExcelColumn(index = 0)
	public int prize;
	/**
	 * 达到条件后获取抽奖次数数量
	 * */
	@ExcelColumn(index = 1)
	public int count;
	
	/**
	 * 清空的时间点
	 * */
	@ExcelColumn(index = 2)
	public String resetTime;
	/**
	 * 帮助内容
	 * */
	@ExcelColumn(index = 3)
	public String helpContent;
	
	/**
	 * 清空类型,1,0点清空;2,永远保留
	 * */
	@ExcelColumn(index = 4)
	public int resetType;
}
