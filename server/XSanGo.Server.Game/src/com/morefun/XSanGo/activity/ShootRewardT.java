package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * @author zhouming
 */
@ExcelTable(fileName = "script/活动和礼包/百步穿杨.xls", beginRow = 2)
public class ShootRewardT {

	/**
	 * 编号
	 */
	@ExcelColumn(index = 0)
	public int id;

	/**
	 * 道具ID
	 */
	@ExcelColumn(index = 1)
	public String itemId;

	/**
	 * 数量
	 */
	@ExcelColumn(index = 2)
	public int num;

	/**
	 * 权重
	 */
	@ExcelColumn(index = 3)
	public int pro;

	/**
	 * 是否公告
	 */
	@ExcelColumn(index = 4)
	public int notice;
}
