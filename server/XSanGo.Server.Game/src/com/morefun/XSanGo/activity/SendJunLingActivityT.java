package com.morefun.XSanGo.activity;

import java.util.Date;

import com.morefun.XSanGo.script.DataFormat;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * @author guofeng.qin
 */
@ExcelTable(fileName = "script/活动和礼包/领军令.xls", sheetName = "领军令节日活动", beginRow = 2)
public class SendJunLingActivityT {
	/** 活动开始时间 */
	@ExcelColumn(index = 0, dataType = DataFormat.DateTime)
	public Date startDate;
	/** 活动结束时间 */
	@ExcelColumn(index = 1, dataType = DataFormat.DateTime)
	public Date endDate;
	/** 类型 */
	@ExcelColumn(index = 2)
	public int type;
}
