package com.morefun.XSanGo.goodsExchange;

import com.morefun.XSanGo.script.ExcelColumn;

/** 兑换所需物品，包含所需物品的id和数量 */
public class ExchangeItemConfig {

	@ExcelColumn(index = 0)
	public int type;
	
	@ExcelColumn(index = 1)
	public String code;

	@ExcelColumn(index = 2)
	public int value;
	
}
