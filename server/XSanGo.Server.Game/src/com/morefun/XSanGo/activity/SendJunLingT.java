package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * @author qinguofeng
 */
@ExcelTable(fileName = "script/活动和礼包/领军令.xls", sheetName = "领军令", beginRow = 2)
public class SendJunLingT {
	/**
	 * 编号
	 * */
	@ExcelColumn(index = 0)
	public int id;
	/**
	 * 开始时间
	 * */
	@ExcelColumn(index = 1)
	public String start;
	/**
	 * 结束时间
	 * */
	@ExcelColumn(index = 2)
	public String end;
	/**
	 * 数量
	 * */
	@ExcelColumn(index = 3)
	public int num;
	/**
	 * 获取元宝权重
	 * */
	@ExcelColumn(index = 4)
	public int chance;
	/**
	 * 最少元宝
	 * */
	@ExcelColumn(index = 5)
	public int min;
	/**
	 * 最大元宝
	 * */
	@ExcelColumn(index = 6)
	public int max;
	/**
	 * 类型
	 * */
	@ExcelColumn(index = 7)
	public int type;
}
