package com.morefun.XSanGo.chat;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/互动和聊天/表情动作脚本.xls", beginRow = 2, sheetName = "表情动作")
public class ChatActionT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public String action_name;

	@ExcelColumn(index = 2)
	public String content;

	@ExcelColumn(index = 3)
	public int world_valid;

	@ExcelColumn(index = 4)
	public int guild_valid;

	@ExcelColumn(index = 5)
	public int private_valid;

	@ExcelColumn(index = 6)
	public int ally_valid;
}
