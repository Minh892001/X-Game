package com.morefun.XSanGo.ladder;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

@ExcelTable(fileName = "script/互动和聊天/群雄争霸脚本.xls", beginRow = 2, sheetName = "等级奖励")
public class LadderLevelAwardT {
	// id
	@ExcelColumn(index = 0)
	public int id;

	// 获得金币
	@ExcelColumn(index = 7)
	public int gold;

	// 获得元宝
	@ExcelColumn(index = 8)
	public int Yuanbao;

	// 获得道具
	@ExcelColumn(index = 9)
	public String item;
	
	// 获得道具 数量
	@ExcelColumn(index = 10)
	public int itemNum;
	
	// 完成条件
	@ExcelColumn(index = 11)
	public int demand;
}
