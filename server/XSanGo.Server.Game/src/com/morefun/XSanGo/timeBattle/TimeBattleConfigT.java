package com.morefun.XSanGo.timeBattle;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/副本和怪物/时空战役.xls", sheetName = "参数配置", beginRow = 2)
public class TimeBattleConfigT {
	@ExcelColumn(index = 0)
	public String resetTime;// 挑战次数重置时间
	
	@ExcelColumn(index = 1)
	public int clearLevel;// 扫荡等级
	
	@ExcelColumn(index = 2)
	public int clearVipLevel;// 扫荡VIP等级
}
