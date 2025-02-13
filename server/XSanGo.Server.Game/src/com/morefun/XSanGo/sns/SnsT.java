package com.morefun.XSanGo.sns;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/互动和聊天/好友脚本.xls", sheetName = "好友基础配置", beginRow = 2)
public class SnsT {

	@ExcelColumn(index = 0)
	public int Lv;
	@ExcelColumn(index = 1)
	public int MaxNum;
}
