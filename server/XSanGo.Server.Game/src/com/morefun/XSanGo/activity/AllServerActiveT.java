package com.morefun.XSanGo.activity;

import java.util.Date;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 全服活动
 * 
 * @author sunjie
 */
@ExcelTable(fileName = "script/活动和礼包/开服活动.xls", sheetName = "活动参数表", beginRow = 2)
public class AllServerActiveT {
	/**
	 * 活动ID
	 * */
	@ExcelColumn(index = 0)
	public int activeId;
	/**
	 * 活动类型
	 * */
	@ExcelColumn(index = 1)
	public int activeType;
	/**
	 * 活动名称
	 * */
	@ExcelColumn(index = 2)
	public String activeName;
	/**
	 * 活动描述
	 * */
	@ExcelColumn(index = 3)
	public String description;
	/**
	 * 开始时间
	 * */
	@ExcelColumn(index = 4)
	public int startTime;
	/**
	 * 结束时间
	 * */
	@ExcelColumn(index = 5)
	public int overTime;
	
	public Date startDate;
	
	public Date endDate;
}
