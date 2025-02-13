package com.morefun.XSanGo.ladder;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/互动和聊天/群雄争霸脚本.xls", beginRow = 2, sheetName = "赛季排行奖励")
public class LadderAwardT {
	// 编号
	@ExcelColumn(index = 0)
	public int id;

	// 等级开始
	@ExcelColumn(index = 1)
	public int start;

	// 等级截止
	@ExcelColumn(index = 2)
	public int end;
	
	// 奖励物品
	@ExcelColumn(index = 3)
	public String items;

}
