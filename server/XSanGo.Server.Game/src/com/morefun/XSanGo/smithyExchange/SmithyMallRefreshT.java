package com.morefun.XSanGo.smithyExchange;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/道具相关/铁匠铺兑换脚本.xls", beginRow = 2)
public class SmithyMallRefreshT {
	//次数
	@ExcelColumn(index = 0)
	public int Time;
	
	//花费元宝
	@ExcelColumn(index = 1)
	public int Cost;
}
