package com.morefun.XSanGo.colorfulEgg;

import java.util.Date;

import com.morefun.XSanGo.script.DataFormat;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/活动和礼包/砸蛋配置表.xls", beginRow = 2, sheetName="活动时间")
public class EggPeriodsT {
	/** id*/
	@ExcelColumn(index = 0)
	public int id;
	
	/** 开始时间*/
	@ExcelColumn(index = 1, dataType = DataFormat.DateTime)
	public Date beginTime;
	
	/** 结束时间*/
	@ExcelColumn(index = 2, dataType = DataFormat.DateTime)
	public Date endTime;
}
