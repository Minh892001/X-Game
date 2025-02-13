package com.morefun.XSanGo.activity;

import java.util.Map;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 全服活动内容
 * 
 * @author sunjie
 */
@ExcelTable(fileName = "script/活动和礼包/开服活动.xls", sheetName = "活动内容", beginRow = 2)
public class AllServerActiveDetailT {
	/**
	 * 活动编号
	 * */
	@ExcelColumn(index = 0)
	public int activeNum;
	/**
	 * 活动ID
	 * */
	@ExcelColumn(index = 1)
	public int activeId;
	/**
	 * 活动描述
	 * */
	@ExcelColumn(index = 2)
	public String description;
	/**
	 * 条件参数
	 * */
	@ExcelColumn(index = 3)
	public int conditionNum;
	/**
	 * 达成条件1
	 * */
	@ExcelColumn(index = 4)
	public String condition1;
	/**
	 * 达成条件2
	 * */
	@ExcelColumn(index = 5)
	public String condition2;
	/**
	 * 活动奖励
	 * */
	@ExcelColumn(index = 6)
	public String reward;
	
	/**
	 * 分解后的奖励列表
	 */
	public Map<String, Integer> rewardMap;
}
