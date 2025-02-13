package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * @author zhouming
 */
@ExcelTable(fileName = "script/活动和礼包/百步穿杨.xls", sheetName = "参数配置", beginRow = 2)
public class MarksManParamT {

	/**
	 * 主公等级
	 */
	@ExcelColumn(index = 0)
	public int lv;

	/**
	 * 主公vip等级
	 */
	@ExcelColumn(index = 1)
	public int vip;

	/**
	 * 开始时间
	 */
	@ExcelColumn(index = 2)
	public String startTime;

	/**
	 * 截止时间
	 */
	@ExcelColumn(index = 3)
	public String endTime;

	/**
	 * 显示数量
	 */
	@ExcelColumn(index = 4)
	public int number;

	/**
	 * 单抽货币类型
	 */
	@ExcelColumn(index = 6)
	public String currency01;

	/**
	 * 十连货币类型
	 */
	@ExcelColumn(index = 7)
	public String currency02;

	/**
	 * 单抽消耗
	 */
	@ExcelColumn(index = 8)
	public int deplete01;

	/**
	 * 十连消耗
	 */
	@ExcelColumn(index = 9)
	public int deplete02;

	/**
	 * 免费抽
	 */
	@ExcelColumn(index = 10)
	public int free;

	/**
	 * 抽一次
	 */
	@ExcelColumn(index = 11)
	public int one;

	/**
	 * 抽十次
	 */
	@ExcelColumn(index = 12)
	public int ten;
	
	/**
	 * 高级单抽货币类型
	 */
	@ExcelColumn(index = 13)
	public String currency03;
	
	/**
	 * 高级十连货币类型
	 */
	@ExcelColumn(index = 14)
	public String currency04;
	
	/**
	 * 高级单抽消耗
	 */
	@ExcelColumn(index = 15)
	public int deplete03;
	
	/**
	 * 高级十连消耗
	 */
	@ExcelColumn(index = 16)
	public int deplete04;
	
	/**
	 * 高级免费抽
	 */
	@ExcelColumn(index = 17)
	public int free1;
	
	/**
	 * 高级抽一次
	 */
	@ExcelColumn(index = 18)
	public int one1;
	
	/**
	 * 高级抽十次
	 */
	@ExcelColumn(index = 19)
	public int ten1;
	
	/**
	 * 基础入榜积分
	 */
	@ExcelColumn(index = 20)
	public int Basis;
	
	/**
	 * 超级百步需求VIP等级
	 */
	@ExcelColumn(index = 21)
	public int needVIP;
	
}
