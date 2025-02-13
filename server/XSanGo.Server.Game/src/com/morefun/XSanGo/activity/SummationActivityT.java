package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelComponet;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/活动和礼包/充值和消费.xls", beginRow = 2)
public class SummationActivityT {
	
	@ExcelComponet(index = 0, columnCount = 3, size = 4)
	public SummationActivityComponentT[] components;
}
