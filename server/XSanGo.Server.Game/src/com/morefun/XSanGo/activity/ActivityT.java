/**
 * 
 */
package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 活动列表
 */
@ExcelTable(fileName = "script/活动和礼包/活动列表.xls", sheetName = "活动列表", beginRow = 2)
public class ActivityT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public String name;
 
	@ExcelColumn(index = 2)
	public int type;

	@ExcelColumn(index = 3)
	public String startTime;

	@ExcelColumn(index = 4)
	public String endTime;

	@ExcelColumn(index = 5)
	public String icon;

	@ExcelColumn(index = 6)
	public String subActivity;// 子活动ID逗号分割

	/** 开服后几天为活动的结束日期 */
	@ExcelColumn(index = 7)
	public int openAfterDays;

	/** 领取完后置尾 0-不置尾，其他需要 */
	@ExcelColumn(index = 8)
	public int isOverTail;
	
	/**可见的渠道ID,分割*/
	@ExcelColumn(index = 9)
	public String channelIds;
	
	/** 活动介绍 */
	@ExcelColumn(index = 10)
	public String intro;
	
	/** 对应api编号 */
	@ExcelColumn(index = 11)
	public int apiId;
}
