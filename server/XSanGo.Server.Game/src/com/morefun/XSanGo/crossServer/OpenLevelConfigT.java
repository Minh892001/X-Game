package com.morefun.XSanGo.crossServer;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 功能开放等级配置
 * 
 * @author guofeng.qin
 */
@ExcelTable(fileName = "script/参数配置脚本/新人引导脚本.xls", sheetName = "功能开放配置", beginRow = 2)
public class OpenLevelConfigT {
	@ExcelColumn(index = 0)
	public String id;
	/** 是否开放 */
	@ExcelColumn(index = 2)
	public int isOpen;
	/** 开放等级 */
	@ExcelColumn(index = 3)
	public int openLevel;
}
