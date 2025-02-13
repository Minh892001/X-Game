package com.morefun.XSanGo.chat;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/互动和聊天/聊天公告走马灯.xls", beginRow = 1, sheetName = "走马灯")
public class ChatAdT {

	@ExcelColumn(index = 0)
	public int id;

	// 类型
	@ExcelColumn(index = 1)
	public int type;

	// 开关 0-关 1-开
	@ExcelColumn(index = 2)
	public int onOff;

	// 显示内容
	@ExcelColumn(index = 3)
	public String content;

}
