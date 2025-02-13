package com.morefun.XSanGo.faction;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 热血公会战累计胜利奖励
 * 
 * @author lixiongming
 *
 */
@ExcelTable(beginRow = 2, fileName = "script/互动和聊天/公会脚本.xls", sheetName = "热血公会战")
public class WarmGvgAwardT {
	@ExcelColumn(index = 0)
	public int winNum;

	@ExcelColumn(index = 1)
	public String itemId;
}
