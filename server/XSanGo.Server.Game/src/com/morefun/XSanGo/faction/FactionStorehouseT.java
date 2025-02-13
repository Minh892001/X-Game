package com.morefun.XSanGo.faction;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 公会栈房
 * 
 * @author lixiongming
 *
 */
@ExcelTable(beginRow = 2, fileName = "script/互动和聊天/公会脚本.xls", sheetName = "公会栈房")
public class FactionStorehouseT {
	@ExcelColumn(index = 0)
	public int level;

	@ExcelColumn(index = 1)
	public String itemId;

	@ExcelColumn(index = 2)
	public String itemName;

	@ExcelColumn(index = 3)
	public int price;

	@ExcelColumn(index = 4)
	public int isFree;

	/** 玩家购买时的荣誉价格 */
	@ExcelColumn(index = 5)
	public int honorPrice;

}
