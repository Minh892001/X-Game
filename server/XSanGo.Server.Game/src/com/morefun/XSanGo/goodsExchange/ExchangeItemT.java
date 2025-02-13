package com.morefun.XSanGo.goodsExchange;

import java.util.Date;

import com.morefun.XSanGo.script.DataFormat;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelComponet;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/活动和礼包/物物兑换.xls", beginRow = 1, sheetName = "物物兑换")
public class ExchangeItemT {
	
	/** 兑换编号 */
	@ExcelColumn(index = 0)
	public String itemNo;
	/** 兑换物类型 */
	@ExcelColumn(index = 1)
	public int itemType;
	/** 兑换物id */
	@ExcelColumn(index = 2)
	public String itemId;
	/** 兑换物数量 */
	@ExcelColumn(index = 3)
	public int num;
	/** 兑换物名称 */
	@ExcelColumn(index = 4)
	public String itemName;
	/** 兑换物名称品质色 */
	@ExcelColumn(index = 5)
	public int colorType;
	/** 是否公告 */
	@ExcelColumn(index = 6)
	public int annoFlag;
	/** 每天兑换次数 */
	@ExcelColumn(index = 7)
	public int dealCountsLim;
	/** 兑换后是否消失 */
	@ExcelColumn(index = 8)
	public int hideFlag;
	/**兑换所需vip等级 */
	@ExcelColumn(index = 9)
	public int vipLevel;
	/**兑换所需主公等级 */
	@ExcelColumn(index = 10)
	public int roleLevel;

	/**兑换物品相关信息，包括id、数量*/
	@ExcelComponet(index = 11, columnCount = 3, size = 4)
	public ExchangeItemConfig[] itemConfigs;
	
	/**兑换物品相关信息，包括id、数量*/
	@ExcelColumn(index = 20)
	public int defaultFlag;
	/**是否显示限时标签*/
	@ExcelColumn(index = 21)
	public int limitTimeFlag;
	@ExcelColumn(index = 22)
	public String startDate;

	@ExcelColumn(index = 23)
	public String endDate;
	
}
