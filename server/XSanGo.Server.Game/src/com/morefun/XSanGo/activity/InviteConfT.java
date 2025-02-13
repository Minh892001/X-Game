package com.morefun.XSanGo.activity;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/活动和礼包/邀请好友.xls", beginRow = 2, sheetName = "限制")
public class InviteConfT {

	@ExcelColumn(index = 0)
	public int minLevel;

	@ExcelColumn(index = 1)
	public String tc;// 被邀请人奖励
}
