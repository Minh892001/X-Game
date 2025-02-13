package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 资源找回，类型名称映射配置
 * 
 * @author guofeng.qin
 */
@ExcelTable(fileName = "script/活动和礼包/资源找回.xls", sheetName = "单项配置", beginRow = 2)
public class ResourceBackConfigT {
	@ExcelColumn(index = 0)
	public int type;
	@ExcelColumn(index = 1)
	public String name;
	@ExcelColumn(index = 2)
	public String icon;
	@ExcelColumn(index = 6)
	public int rate1;
	@ExcelColumn(index = 7)
	public int rate2;
}
