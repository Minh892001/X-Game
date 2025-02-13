/**
 * 
 */
package com.morefun.XSanGo.hero.market;

import java.util.Date;

import com.morefun.XSanGo.script.DataFormat;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 限时武将设置
 * 
 * @author lixiongming
 *
 */
@ExcelTable(fileName = "script/武将相关/抽卡脚本.xls", sheetName = "魂匣配置", beginRow = 2)
public class LimitHeroConfT {
	@ExcelColumn(index = 0)
	public int vipLevel;

	@ExcelColumn(index = 1)
	public int price;

	/** 每周星期几刷新,分割 */
	@ExcelColumn(index = 2)
	public String refreshWeek;

	/** 每周刷新时间 */
	@ExcelColumn(index = 3)
	public String weekTime;

	/** 每日刷新时间 */
	@ExcelColumn(index = 4)
	public String dayTime;

	@ExcelColumn(index = 5)
	public int heroMinCount;

	@ExcelColumn(index = 6)
	public int heroMaxCount;

	@ExcelColumn(index = 7)
	public int equipMinYuanbao;

	@ExcelColumn(index = 8)
	public int equipMaxYuanbao;

	@ExcelColumn(index = 9)
	public int weekWeight;

	@ExcelColumn(index = 10)
	public int todayWeight;

	/** 运营活动开始时间 */
	@ExcelColumn(index = 11, dataType = DataFormat.DateTime)
	public Date startDate;

	/** 运营活动结束时间 */
	@ExcelColumn(index = 12, dataType = DataFormat.DateTime)
	public Date endDate;

	/** 运营活动物品 */
	@ExcelColumn(index = 13)
	public String items;

	/** 无VIP限制开始时间 */
	@ExcelColumn(index = 14, dataType = DataFormat.DateTime)
	public Date vipStartDate;

	/** 无VIP限制结束时间 */
	@ExcelColumn(index = 15, dataType = DataFormat.DateTime)
	public Date vipEndDate;

	/** 无VIP限制抽卡次数 */
	@ExcelColumn(index = 16)
	public int notVipNum;
	
	/** 无VIP限制时是否免费 0-不免费 1-免费*/
	@ExcelColumn(index = 17)
	public int isFree;
	
	@ExcelColumn(index = 18)
	public String itemId;
	
	@ExcelColumn(index = 19)
	public int itemNum;
	
	/**
	 * 物品每天购买上限 -1-不限制
	 */
	@ExcelColumn(index = 20)
	public int itemMaxBuy;
	
	@ExcelColumn(index = 21)
	public int itemVip;
}
