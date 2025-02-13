package com.morefun.XSanGo.announce;

import java.util.Date;

import com.morefun.XSanGo.script.DataFormat;
import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "登录界面公告.xls", sheetName = "登录界面公告")
public class AnnounceT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public String title;

	@ExcelColumn(index = 2)
	public String content;

	@ExcelColumn(index = 3)
	public int open;

	@ExcelColumn(index = 4, dataType = DataFormat.DateTime)
	public Date beginTime;

	@ExcelColumn(index = 5, dataType = DataFormat.DateTime)
	public Date endTime;

}
