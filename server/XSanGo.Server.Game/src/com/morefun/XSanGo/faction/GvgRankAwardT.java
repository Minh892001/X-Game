package com.morefun.XSanGo.faction;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 公会战排名奖励
 * 
 * @author lixiongming
 *
 */
@ExcelTable(beginRow = 2, fileName = "script/互动和聊天/公会脚本.xls", sheetName = "公会战排名奖励")
public class GvgRankAwardT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public int rank;

	@ExcelColumn(index = 2)
	public String items;

	//活动奖励
	@ExcelColumn(index = 3)
	public String activityItems;
}
