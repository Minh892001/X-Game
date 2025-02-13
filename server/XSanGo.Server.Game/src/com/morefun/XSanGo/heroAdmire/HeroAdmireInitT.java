package com.morefun.XSanGo.heroAdmire;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/活动和礼包/名将仰慕.xls", beginRow = 2, sheetName = "刷新配置")
public class HeroAdmireInitT {
	/** 3星数量*/
	@ExcelColumn(index = 0)
	public int num_3S;

	/** 2星数量*/
	@ExcelColumn(index = 1)
	public int num_2S;

	/** 1星数量*/
	@ExcelColumn(index = 2)
	public int num_1S;
	
	/** 武将刷新时间*/
	@ExcelColumn(index = 3)
	public String heroRefreshDate;
	
	/** 道具刷新时间*/
	@ExcelColumn(index = 4)
	public String itemRefreshDate;
}
