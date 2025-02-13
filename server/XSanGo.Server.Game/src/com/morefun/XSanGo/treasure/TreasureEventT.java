package com.morefun.XSanGo.treasure;

import com.morefun.XSanGo.script.ExcelColumn;
import com.morefun.XSanGo.script.ExcelTable;

/**
 * 寻宝事件
 * 
 * @author lixiongming
 *
 */
@ExcelTable(fileName = "script/互动和聊天/寻宝大冒险脚本.xls", sheetName = "事件参数", beginRow = 2)
public class TreasureEventT {
	@ExcelColumn(index = 0)
	public int id;

	@ExcelColumn(index = 1)
	public String describe;

	/**
	 * 基础奖励增加百分比
	 */
	@ExcelColumn(index = 2)
	public int result;

	@ExcelColumn(index = 3)
	public int weight;
}
