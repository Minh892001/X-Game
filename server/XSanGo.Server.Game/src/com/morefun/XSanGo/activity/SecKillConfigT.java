package com.morefun.XSanGo.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.morefun.XSanGo.script.DataFormat;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/活动和礼包/天天秒杀.xls", sheetName = "秒杀设置", beginRow = 2)
public class SecKillConfigT {
	@ExcelColumn(index = 0)
	public int type;

	@ExcelColumn(index = 1)
	public int isOpen;

	@ExcelColumn(index = 2, dataType = DataFormat.DateTime)
	public Date startDate;

	@ExcelColumn(index = 3, dataType = DataFormat.DateTime)
	public Date endDate;

	public List<SecKillItemT> secKillItemTs = new ArrayList<SecKillItemT>();
}
