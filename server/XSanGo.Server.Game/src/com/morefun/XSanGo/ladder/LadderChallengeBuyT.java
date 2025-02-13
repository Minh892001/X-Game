package com.morefun.XSanGo.ladder;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/互动和聊天/群雄争霸脚本.xls", beginRow = 2, sheetName = "购买挑战次数")
public class LadderChallengeBuyT {
	// 次数
	@ExcelColumn(index = 0)
	public int time;

	// 花费元宝
	@ExcelColumn(index = 1)
	public int costYuanbao;

	// 获得挑战次数
	@ExcelColumn(index = 2)
	public int obtain;

	// 需要vip等级
	@ExcelColumn(index = 3)
	public int vipLv;
}
