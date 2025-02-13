package com.morefun.XSanGo.vip;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/参数配置脚本/对照说明表.xls", sheetName = "VIP品质颜色", beginRow = 2)
public class VipColorT {

	@ExcelColumn(index = 1)
	public int vipQuality;
	@ExcelColumn(index = 2)
	public String ColorValue;
}
