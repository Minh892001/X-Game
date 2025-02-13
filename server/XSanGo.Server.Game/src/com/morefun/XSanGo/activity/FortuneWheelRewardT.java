package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * @author qinguofeng
 */
@ExcelTable(fileName = "script/活动和礼包/幸运大转盘.xls", sheetName = "商品列表", beginRow = 2)
public class FortuneWheelRewardT {

	/**
	 * 类型
	 * */
	@ExcelColumn(index = 0)
	public int type;
	/**
	 * ID
	 * */
	@ExcelColumn(index = 1)
	public int id;
	/**
	 * 道具ID
	 * */
	@ExcelColumn(index = 2)
	public String templateId;
	/**
	 * 数量
	 * */
	@ExcelColumn(index = 3)
	public int num;
	/**
	 * 权重
	 * */
	@ExcelColumn(index = 4)
	public int weight;
	/**
	 * 是否公告
	 * */
	@ExcelColumn(index = 5)
	public int notice;

	/** 类型 */
	@ExcelColumn(index = 6)
	public int itemType;
}
