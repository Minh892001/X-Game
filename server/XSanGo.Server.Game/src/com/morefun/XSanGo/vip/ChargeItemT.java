package com.morefun.XSanGo.vip;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/参数配置脚本/充值选项.xls", sheetName = "充值选项", beginRow = 2)
public class ChargeItemT {

	@ExcelColumn(index = 0)
	public int id;
	@ExcelColumn(index = 1)
	public String name;

	@ExcelColumn(index = 2)
	@Deprecated
	public int effectAtFirst;

	@ExcelColumn(index = 3)
	public int monthCard;

	@ExcelColumn(index = 4)
	public int quality;

	@ExcelColumn(index = 5)
	public boolean recommand;

	@ExcelColumn(index = 6)
	public int rmb;

	@ExcelColumn(index = 7)
	public int yuanbao;

	@ExcelColumn(index = 8)
	public int normalReturnYuanbao;

	@ExcelColumn(index = 9)
	public int limitReturnYuanbao;

	@ExcelColumn(index = 10)
	public int timeLimit;

	@ExcelColumn(index = 11)
	public String icon;

	public boolean isMonthCard() {
		return this.monthCard == 1;
	}

	@Deprecated
	public boolean isEffectAtFirst() {
		return this.effectAtFirst == 1;
	}

}
