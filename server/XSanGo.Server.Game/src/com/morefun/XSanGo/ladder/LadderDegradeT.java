package com.morefun.XSanGo.ladder;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/互动和聊天/群雄争霸脚本.xls", beginRow = 2, sheetName = "新赛季降级规则")
public class LadderDegradeT {
	// 起始
	@ExcelColumn(index = 0)
	public int startRank;

	// 截止
	@ExcelColumn(index = 1)
	public int endRank;

	// 下赛季初始等级
	@ExcelColumn(index = 2)
	public int lvStart;
}
