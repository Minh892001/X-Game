package com.morefun.XSanGo.makewine;

import java.util.Date;

import com.morefun.XSanGo.script.DataFormat;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 基础配置
 * @author zhuzhi.yang
 *
 */
@ExcelTable(fileName = "script/活动和礼包/煮酒论英雄.xls", sheetName = "基础配置", beginRow = 2)
public class MakeWineConfigT {
	
	@ExcelColumn(index = 0)
	public int showLevel;

	@ExcelColumn(index = 1)
	public int openLevel;
	
	@ExcelColumn(index = 2, dataType = DataFormat.DateTime)
	public Date showTime;
	
	@ExcelColumn(index = 3, dataType = DataFormat.DateTime)
	public Date hideTime;
	
	@ExcelColumn(index = 4, dataType = DataFormat.DateTime)
	public Date startTime;
	
	@ExcelColumn(index = 5, dataType = DataFormat.DateTime)
	public Date endTime;
	
	@ExcelColumn(index = 6)
	public int shareInterval;
	
	@ExcelColumn(index = 7)
	public int shareNum;
	
	@ExcelColumn(index = 8)
	public int refreshTime;
	
	@ExcelColumn(index = 9)
	public int mixScore;
	
	@ExcelColumn(index = 10)
	public int upNum;
	
	@ExcelColumn(index = 11)
	public int upPrice;
	
}
