package com.morefun.XSanGo.activity;

import java.util.Date;

import com.morefun.XSanGo.script.DataFormat;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 资源找回，时间配置
 * 
 * @author guofeng.qin
 */
@ExcelTable(fileName = "script/活动和礼包/资源找回.xls", sheetName = "时间列表", beginRow = 2)
public class ResourceBackTimeT {
	/** ID */
	@ExcelColumn(index = 0)
	public int id;
	/** 开始时间 */
	@ExcelColumn(index = 1, dataType = DataFormat.OnlyDate)
	public Date startTime;
	/** 结束时间 */
	@ExcelColumn(index = 2, dataType = DataFormat.OnlyDate)
	public Date endTime;
}
