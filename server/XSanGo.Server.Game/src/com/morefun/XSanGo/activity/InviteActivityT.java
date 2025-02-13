package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/活动和礼包/邀请好友.xls", beginRow = 2, sheetName = "邀请好友脚本")
public class InviteActivityT {

	@ExcelColumn(index = 0)
	public int num;

	@ExcelColumn(index = 1)
	public String tc;
}
