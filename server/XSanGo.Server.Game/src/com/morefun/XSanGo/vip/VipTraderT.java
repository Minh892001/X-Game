package com.morefun.XSanGo.vip;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/参数配置脚本/VIP脚本.xls", sheetName = "VIP商城", beginRow = 2)
public class VipTraderT {

	@ExcelColumn(index = 0)
	public int id;
	@ExcelColumn(index = 1)
	public String itemId;
	@ExcelColumn(index = 2)
	public String name;
	@ExcelColumn(index = 3)
	public int numMin;
	@ExcelColumn(index = 4)
	public int numMax;
	@ExcelColumn(index = 5)
	public int pro;
	@ExcelColumn(index = 6)
	public int coinType;
	@ExcelColumn(index = 7)
	public int price;
	@ExcelColumn(index = 8)
	public int vipMin;

	@ExcelColumn(index = 9)
	public int buyVipLevel;

}
