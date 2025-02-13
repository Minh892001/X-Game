package com.morefun.XSanGo.activity;

import java.util.Date;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 全服活动半价礼包内容
 * 
 * @author sunjie
 */
@ExcelTable(fileName = "script/活动和礼包/开服活动.xls", sheetName = "半价礼包", beginRow = 2)
public class AllServerActiveSaleT {
	/**
	 * 开服天数
	 * */
	@ExcelColumn(index = 0)
	public int openDay;
	/**
	 * 道具ID
	 * */
	@ExcelColumn(index = 1)
	public String itemId;
	/**
	 * 道具数量
	 * */
	@ExcelColumn(index = 2)
	public int num;
	/**
	 * 货币类型
	 * */
	@ExcelColumn(index = 3)
	public int coinType;
	/**
	 * 原价
	 * */
	@ExcelColumn(index = 4)
	public int bePrice;
	/**
	 * 现价
	 * */
	@ExcelColumn(index = 5)
	public int price;
	
	public Date openDate;
}
