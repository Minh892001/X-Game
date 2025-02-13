package com.morefun.XSanGo.buyJinbi;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/参数配置脚本/购买脚本.xls", beginRow = 2, sheetName = "点金手")
public class BuyJInbiT {
	@ExcelColumn(index = 0)
	public int times;
	
	// 花费元宝
	@ExcelColumn(index = 1)
	public int costYuanbao;

	// 最少金币
	@ExcelColumn(index = 2)
	public int minJinbi;

	// 暴击概率
	@ExcelColumn(index = 3)
	public int crit;

	// 暴击倍率
	@ExcelColumn(index = 4)
	public String rate;
}
